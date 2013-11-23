/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.CuriousHouse;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

//ExCuriousHouseResult dh(Sddd)
public class ExCuriousHouseResult extends L2GameServerPacket {
    public void ExCuriousHouseResult() {
    }

    @Override
    protected void writeImpl() {
        writeEx(0x12A);

        writeD(0);
        writeH(1);

        writeD(1);

        writeD(268483021);
        writeD(1);
        writeD(146);
        writeD(146);
        writeD(2);
    }

    @Override
    public String getType() {
        return "[S] FE:12A ExCuriousHouseResult";
    }
}
/*
char __cdecl sub_20468110(int a1, int a2)
{
  int i; // ebx@3
  int v3; // esi@3
  char v5; // [sp+0h] [bp-74h]@1
  char v6; // [sp+Ch] [bp-68h]@5
  int v7; // [sp+4Ch] [bp-28h]@4
  int v8; // [sp+50h] [bp-24h]@5
  int v9; // [sp+54h] [bp-20h]@5
  int v10; // [sp+58h] [bp-1Ch]@5
  int v11; // [sp+5Ch] [bp-18h]@3
  int v12; // [sp+60h] [bp-14h]@3
  char *v13; // [sp+64h] [bp-10h]@1
  int v14; // [sp+70h] [bp-4h]@1

  v13 = &v5;
  v14 = 0;
  if ( *(_DWORD *)GNetworkLog )
    FOutputDevice__Logf(*(_DWORD *)GNetworkLog, L"(Receive)%s", L"ExCuriousHouseResult", *(_DWORD *)&v5);
  v12 = 0;
  v3 = sub_204668D0(a2, *(_DWORD *)(a1 + 72), (int)"hd");
  (*(void (__thiscall **)(int, int))(*(_DWORD *)GL2Console + 3744))(GL2Console, v11);
  (*(void (**)(void))(*(_DWORD *)GL2Console + 3748))();
  for ( i = 0; ; ++i )
  {
    v7 = i;
    if ( i >= v12 )
      break;
    v3 = sub_204668D0(v3, *(_DWORD *)(a1 + 72), (int)"Sddd");
    (*(void (__thiscall **)(int, char *, int, int, int))(*(_DWORD *)GL2Console + 3756))(GL2Console, &v6, v8, v9, v10);
  }
  (*(void (**)(void))(*(_DWORD *)GL2Console + 3752))();
  return 0;
}
 */
