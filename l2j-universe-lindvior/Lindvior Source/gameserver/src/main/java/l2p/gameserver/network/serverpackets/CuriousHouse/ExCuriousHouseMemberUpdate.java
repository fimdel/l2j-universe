/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.CuriousHouse;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class ExCuriousHouseMemberUpdate extends L2GameServerPacket {
    @Override
    protected void writeImpl() {
        writeEx(0x128);
        writeD(0);
        for (; ; ) {
            writeD(268492063);
            writeS("");
            writeD(200000);
            writeD(200000);
            writeD(77);
            writeD(8888);
        }
    }

    @Override
    public String getType() {
        return "[S] FE:128 ExCuriousHouseMemberUpdate";
    }
}
/*
char __cdecl sub_20468460(int a1, int a2)
{
  int i; // ebx@3
  int v3; // esi@3
  char v5; // [sp+0h] [bp-78h]@1
  char v6; // [sp+Ch] [bp-6Ch]@5
  int v7; // [sp+4Ch] [bp-2Ch]@4
  int v8; // [sp+50h] [bp-28h]@5
  int v9; // [sp+54h] [bp-24h]@5
  int v10; // [sp+58h] [bp-20h]@5
  int v11; // [sp+5Ch] [bp-1Ch]@5
  int v12; // [sp+60h] [bp-18h]@5
  int v13; // [sp+64h] [bp-14h]@3
  char *v14; // [sp+68h] [bp-10h]@1
  int v15; // [sp+74h] [bp-4h]@1

  v14 = &v5;
  v15 = 0;
  if ( *(_DWORD *)GNetworkLog )
    FOutputDevice__Logf(*(_DWORD *)GNetworkLog, L"(Receive)%s", L"ExCuriousHouseMemberList", *(_DWORD *)&v5);
  v13 = 0;
  v3 = sub_204668D0(a2, *(_DWORD *)(a1 + 72), (int)L"d");
  (*(void (**)(void))(*(_DWORD *)GL2Console + 3724))();
  for ( i = 0; ; ++i )
  {
    v7 = i;
    if ( i >= v13 )
      break;
    v3 = sub_204668D0(v3, *(_DWORD *)(a1 + 72), (int)"dSdddd");
    (*(void (__thiscall **)(int, int, char *, int, int, int, int))(*(_DWORD *)GL2Console + 3732))(
      GL2Console,
      v8,
      &v6,
      v9,
      v10,
      v11,
      v12);
  }
  (*(void (**)(void))(*(_DWORD *)GL2Console + 3728))();
  return 0;
}
 */
