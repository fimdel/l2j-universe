package l2p.gameserver.model.entity.events.objects;

import gnu.trove.map.hash.TIntIntHashMap;
import org.dom4j.Element;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 14.09.12
 * Time: 18:24
 */
public final class CampaignTaskObject
        implements Serializable {
    private static final long serialVersionUID = 1L;
    private final TIntIntHashMap _playersProgress = new TIntIntHashMap();
    private final int _id;
    private final int _maxProgress;
    private int _currentProgress;

    public CampaignTaskObject(int id, int maxProgress) {
        this._id = id;
        this._maxProgress = maxProgress;
    }

    public int getId() {
        return this._id;
    }

    public int getMaxProgress() {
        return this._maxProgress;
    }

    public int getCurrentProgress() {
        return this._currentProgress;
    }

    public int getCurrentProgress(int playerObjectId) {
        return this._playersProgress.get(playerObjectId);
    }

    public void incCurrentProgress(int playerObjectId, int val) {
        int oldProgress = this._currentProgress;
        this._currentProgress += val;
        this._currentProgress = Math.min(this._currentProgress, this._maxProgress);

        int playerProgress = this._playersProgress.get(playerObjectId) + (this._currentProgress - oldProgress);
        this._playersProgress.put(playerObjectId, playerProgress);
    }

    public boolean isFinished() {
        return this._currentProgress >= this._maxProgress;
    }

    public void refresh() {
        this._currentProgress = 0;
        this._playersProgress.clear();
    }

    public static CampaignTaskObject parse(Element element) {
        int id = Integer.parseInt(element.attributeValue("id"));
        int maxProgress = Integer.parseInt(element.attributeValue("max_progress"));
        return new CampaignTaskObject(id, maxProgress);
    }
}
