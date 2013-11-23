package services;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.scripts.Functions;

public class VitaminManager extends Functions {
    private static final int PetCoupon = 13273;
    private static final int SpecialPetCoupon = 14065;

    private static final int WeaselNeck = 13017;
    private static final int PrincNeck = 13018;
    private static final int BeastNeck = 13019;
    private static final int FoxNeck = 13020;

    private static final int KnightNeck = 13548;
    private static final int SpiritNeck = 13549;
    private static final int OwlNeck = 13550;
    private static final int TurtleNeck = 13551;

    public void giveWeasel() {
        Player player = getSelf();
        NpcInstance npc = getNpc();

        String htmltext;
        if (getItemCount(player, PetCoupon) > 0) {
            removeItem(player, PetCoupon, 1);
            addItem(player, WeaselNeck, 1);
            htmltext = npc.getNpcId() + "-ok.htm";
        } else
            htmltext = npc.getNpcId() + "-no.htm";

        npc.showChatWindow(player, "default/" + htmltext);
    }

    public void givePrinc() {
        Player player = getSelf();
        NpcInstance npc = getNpc();

        String htmltext;
        if (getItemCount(player, PetCoupon) > 0) {
            removeItem(player, PetCoupon, 1);
            addItem(player, PrincNeck, 1);
            htmltext = npc.getNpcId() + "-ok.htm";
        } else
            htmltext = npc.getNpcId() + "-no.htm";

        npc.showChatWindow(player, "default/" + htmltext);
    }

    public void giveBeast() {
        Player player = getSelf();
        NpcInstance npc = getNpc();

        String htmltext;
        if (getItemCount(player, PetCoupon) > 0) {
            removeItem(player, PetCoupon, 1);
            addItem(player, BeastNeck, 1);
            htmltext = npc.getNpcId() + "-ok.htm";
        } else
            htmltext = npc.getNpcId() + "-no.htm";

        npc.showChatWindow(player, "default/" + htmltext);
    }

    public void giveFox() {
        Player player = getSelf();
        NpcInstance npc = getNpc();

        String htmltext;
        if (getItemCount(player, PetCoupon) > 0) {
            removeItem(player, PetCoupon, 1);
            addItem(player, FoxNeck, 1);
            htmltext = npc.getNpcId() + "-ok.htm";
        } else
            htmltext = npc.getNpcId() + "-no.htm";

        npc.showChatWindow(player, "default/" + htmltext);
    }

    public void giveKnight() {
        Player player = getSelf();
        NpcInstance npc = getNpc();

        String htmltext;
        if (getItemCount(player, SpecialPetCoupon) > 0) {
            removeItem(player, SpecialPetCoupon, 1);
            addItem(player, KnightNeck, 1);
            htmltext = npc.getNpcId() + "-ok.htm";
        } else
            htmltext = npc.getNpcId() + "-no.htm";

        npc.showChatWindow(player, "default/" + htmltext);
    }

    public void giveSpirit() {
        Player player = getSelf();
        NpcInstance npc = getNpc();

        String htmltext;
        if (getItemCount(player, SpecialPetCoupon) > 0) {
            removeItem(player, SpecialPetCoupon, 1);
            addItem(player, SpiritNeck, 1);
            htmltext = npc.getNpcId() + "-ok.htm";
        } else
            htmltext = npc.getNpcId() + "-no.htm";

        npc.showChatWindow(player, "default/" + htmltext);
    }

    public void giveOwl() {
        Player player = getSelf();
        NpcInstance npc = getNpc();

        String htmltext;
        if (getItemCount(player, SpecialPetCoupon) > 0) {
            removeItem(player, SpecialPetCoupon, 1);
            addItem(player, OwlNeck, 1);
            htmltext = npc.getNpcId() + "-ok.htm";
        } else
            htmltext = npc.getNpcId() + "-no.htm";

        npc.showChatWindow(player, "default/" + htmltext);
    }

    public void giveTurtle() {
        Player player = getSelf();
        NpcInstance npc = getNpc();

        String htmltext;
        if (getItemCount(player, SpecialPetCoupon) > 0) {
            removeItem(player, SpecialPetCoupon, 1);
            addItem(player, TurtleNeck, 1);
            htmltext = npc.getNpcId() + "-ok.htm";
        } else
            htmltext = npc.getNpcId() + "-no.htm";

        npc.showChatWindow(player, "default/" + htmltext);
    }
}