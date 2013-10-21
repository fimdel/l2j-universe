package lineage2.gameserver.model.quest.dynamic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Дмитрий
 * @date 28.10.12  18:46
 */
public class DynamicQuestTask {
	public final int questId;
	public final int taskId;
	private final int maxPoints;
	private int currentPoints;
	private int addMode;
	private List<DynamicQuestParticipant> taskParticipants;
	private ReentrantLock lock = new ReentrantLock();

	public DynamicQuestTask(int taskId, int questId, int maxPoints, int addMode) {
		this.questId = questId;
		this.taskId = taskId;
		this.maxPoints = maxPoints;
		this.addMode = addMode;
		taskParticipants = new ArrayList<>();
	}

	public void clear() {
		currentPoints = 0;
		taskParticipants.clear();
	}

	public int getMaxPoints() {
		return maxPoints;
	}

	public int getCurrentPoints() {
		return currentPoints;
	}

	public void increasePoints(DynamicQuestParticipant player, int points) {
		if (!isCompleted()) {
			lock.lock();
			if (!taskParticipants.contains(player)) {
				if (currentPoints + points > maxPoints) {
					points = maxPoints - currentPoints;
				}
				taskParticipants.add(player);
				currentPoints += points;
				player.increaseCurrentPoints(points);
			} else if (addMode != DynamicQuest.TASK_INCREASE_MODE_ONCE_PER_CHAR) {
				if (currentPoints + points > maxPoints) {
					points = maxPoints - currentPoints;
				}
				player.increaseCurrentPoints(points);
				currentPoints += points;
			}
			if (currentPoints == maxPoints) {
				DynamicQuestController.getInstance().taskCompleted(questId, taskId);
			}
			lock.unlock();
		}
	}

	public boolean isCompleted() {
		return currentPoints == maxPoints;
	}
}
