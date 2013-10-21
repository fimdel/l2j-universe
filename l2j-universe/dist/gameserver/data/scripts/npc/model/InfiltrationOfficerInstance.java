package npc.model;

import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;

public class InfiltrationOfficerInstance extends NpcInstance
{
	private static final long serialVersionUID = 1L;

	public InfiltrationOfficerInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}

	@Override
	protected void onDeath(Creature killer)
	{
		Reflection reflection = getReflection();
		if(reflection != ReflectionManager.DEFAULT)
			reflection.collapse();

		super.onDeath(killer);
	}

	@Override
	public boolean isInvul()
	{
		return false;
	}
}
