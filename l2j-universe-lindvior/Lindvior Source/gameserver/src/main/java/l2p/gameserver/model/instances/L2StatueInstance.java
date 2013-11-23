package l2p.gameserver.model.instances;

import l2p.gameserver.dao.CharacterDAO;
import l2p.gameserver.templates.npc.NpcTemplate;

/**
 * @author Darvin
 */
@SuppressWarnings("serial")
public class L2StatueInstance extends NpcInstance {
    private int _recordId, _socialId, _socialFrame, _sex, _hairStyle, _hairColor, _face, _necklace = 0, _head = 0, _classId = 0,
            _rHand = 0, _lHand = 0, _gloves = 0, _chest = 0, _pants = 0, _boots = 0, _cloak = 0, _hair1 = 0, _hair2 = 0, _race = 0;

    public L2StatueInstance(int objectId, NpcTemplate template, int playerObjId, int loc[], int items[], int appearance[]) {
        super(objectId, template);

        _recordId = loc[4];
        _socialId = 0;
        _socialFrame = 0;
        _necklace = items[0];
        _head = items[1];
        _rHand = items[2];
        _lHand = items[3];
        _gloves = items[4];
        _chest = items[5];
        _pants = items[6];
        _boots = items[7];
        _cloak = items[8];
        _hair1 = items[9];
        _hair2 = items[10];

        setName(CharacterDAO.getInstance().getNameByObjectId(playerObjId));
        _classId = appearance[0];
        _race = appearance[1];
        _sex = appearance[2];
        _hairStyle = appearance[3];
        _hairColor = appearance[4];
        _face = appearance[5];

        setIsInvul(true);

        setXYZ(loc[0], loc[1], loc[2]);
        setHeading(loc[3]);

        getTemplate().getBaseWalkSpd();
        getTemplate().getBaseRunSpd();

        spawnMe();
    }

    public int getRecordId() {
        return _recordId;
    }

    public int getSocialId() {
        return _socialId;
    }

    public int getSocialFrame() {
        return _socialFrame;
    }

    public int getClassId() {
        return _classId;
    }

    public int getRace() {
        return _race;
    }

    public int getSex() {
        return _sex;
    }

    public int getHairStyle() {
        return _hairStyle;
    }

    public int getHairColor() {
        return _hairColor;
    }

    public int getFace() {
        return _face;
    }

    public int getNecklace() {
        return _necklace;
    }

    public int getHead() {
        return _head;
    }

    public int getRHand() {
        return _rHand;
    }

    public int getLHand() {
        return _lHand;
    }

    public int getGloves() {
        return _gloves;
    }

    public int getChest() {
        return _chest;
    }

    public int getPants() {
        return _pants;
    }

    public int getBoots() {
        return _boots;
    }

    public int getCloak() {
        return _cloak;
    }

    public int getHair1() {
        return _hair1;
    }

    public int getHair2() {
        return _hair2;
    }
}
