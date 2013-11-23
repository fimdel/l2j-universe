package l2p.loginserver.clientpackets;

import l2p.loginserver.Config;
import l2p.loginserver.GameServerManager;
import l2p.loginserver.IpBanManager;
import l2p.loginserver.L2LoginClient;
import l2p.loginserver.L2LoginClient.LoginClientState;
import l2p.loginserver.accounts.Account;
import l2p.loginserver.accounts.SessionManager;
import l2p.loginserver.accounts.SessionManager.Session;
import l2p.loginserver.crypt.PasswordHash;
import l2p.loginserver.gameservercon.GameServer;
import l2p.loginserver.gameservercon.lspackets.GetAccountInfo;
import l2p.loginserver.serverpackets.LoginFail.LoginFailReason;
import l2p.loginserver.serverpackets.LoginOk;
import l2p.loginserver.utils.Log;

import javax.crypto.Cipher;

/**
 * Format: b[128]ddddddhc
 * b[128]: the rsa encrypted block with the login an password
 */
public class RequestAuthLogin extends L2LoginClientPacket {
    private byte[] _raw = new byte[128];

    @Override
    protected void readImpl() {
        readB(_raw);
        readD();
        readD();
        readD();
        readD();
        readD();
        readD();
        readH();
        readC();
    }

    @SuppressWarnings("unused")
    @Override
    protected void runImpl() throws Exception {
        L2LoginClient client = getClient();

        byte[] decrypted;
        try {
            Cipher rsaCipher = Cipher.getInstance("RSA/ECB/nopadding");
            rsaCipher.init(Cipher.DECRYPT_MODE, client.getRSAPrivateKey());
            decrypted = rsaCipher.doFinal(_raw, 0x00, 0x80);
        } catch (Exception e) {
            client.closeNow(true);
            return;
        }

        String user = new String(decrypted, 0x5E, 14).trim();
        user = user.toLowerCase();
        String password = new String(decrypted, 0x6C, 16).trim();
        int ncotp = decrypted[0x7c];
        ncotp |= decrypted[0x7d] << 8;
        ncotp |= decrypted[0x7e] << 16;
        ncotp |= decrypted[0x7f] << 24;

        int currentTime = (int) (System.currentTimeMillis() / 1000L);

        Account account = new Account(user);
        account.restore();

        String passwordHash = Config.DEFAULT_CRYPT.encrypt(password);

        if (account.getPasswordHash() == null)
            if (Config.AUTO_CREATE_ACCOUNTS && user.matches(Config.ANAME_TEMPLATE) && password.matches(Config.APASSWD_TEMPLATE)) {
                account.setAllowedIP("");
                account.setPasswordHash(passwordHash);
                account.save();
            } else {
                client.close(LoginFailReason.REASON_USER_OR_PASS_WRONG);
                return;
            }

        boolean passwordCorrect = account.getPasswordHash().equals(passwordHash);

        if (!passwordCorrect)
            // проверяем не зашифрован ли пароль одним из устаревших но поддерживаемых алгоритмов
            for (PasswordHash c : Config.LEGACY_CRYPT)
                if (c.compare(password, account.getPasswordHash())) {
                    passwordCorrect = true;
                    account.setPasswordHash(passwordHash);
                    break;
                }

        if (!IpBanManager.getInstance().tryLogin(client.getIpAddress(), passwordCorrect)) {
            client.closeNow(false);
            return;
        }

        if (!passwordCorrect) {
            client.close(LoginFailReason.REASON_USER_OR_PASS_WRONG);
            return;
        }

        if (account.getAccessLevel() < 0) {
            client.close(LoginFailReason.REASON_ACCESS_FAILED);
            return;
        }

        if (account.getBanExpire() > currentTime) {
            client.close(LoginFailReason.REASON_ACCESS_FAILED);
            return;
        }

        if (!account.isAllowedIP(client.getIpAddress())) {
            client.close(LoginFailReason.REASON_ATTEMPTED_RESTRICTED_IP);
            return;
        }

        for (GameServer gs : GameServerManager.getInstance().getGameServers())
            if (gs.getProtocol() >= 2 && gs.isAuthed())
                gs.sendPacket(new GetAccountInfo(user));

        account.setLastAccess(currentTime);
        account.setLastIP(client.getIpAddress());

        Log.LogAccount(account);

        Session session = SessionManager.getInstance().openSession(account);

        client.setAuthed(true);
        client.setLogin(user);
        client.setAccount(account);
        client.setSessionKey(session.getSessionKey());
        client.setState(LoginClientState.AUTHED);

        client.sendPacket(new LoginOk(client.getSessionKey()));
    }
}
