package npc.model;

import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.templates.npc.NpcTemplate;

public final class OrbisInstance extends MonsterInstance
{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private final int _weaponId;
  Creature attacker = getPlayer();
  
  public OrbisInstance(int ObjectID, NpcTemplate temp)
  {
    super(ObjectID, temp);
    //_weaponIdT = getParameter("weapon_id", 17372);
	_weaponId = getParameter("weapon_id", 15280);
  }

  public void onUseSkill()
  {
	if (attacker != null && attacker != this)
		startAttackStanceTask();
  }
  
  public void startAttackStanceTask()
  {
    equipWeapon();
    super.startAttackStanceTask();
  }

  public void stopAttackStanceTask()
  {
	unequipWeapon();
    super.stopAttackStanceTask();
  }

  private void unequipWeapon()
  {
    if (isAlikeDead())
      return;
    setRHandId(0);
    broadcastCharInfo();
  }

  private void equipWeapon()
  {
	setRHandId(_weaponId);
    broadcastCharInfo();
  }
}