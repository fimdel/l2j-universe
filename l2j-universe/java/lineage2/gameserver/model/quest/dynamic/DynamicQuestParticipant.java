package lineage2.gameserver.model.quest.dynamic;

/**
 * @author Дмитрий
 * @date 31.10.12  23:51
 */
public class DynamicQuestParticipant implements Comparable<DynamicQuestParticipant> {
	private String name;
	private int currentPoints;
	private int additionalPoints;

	public DynamicQuestParticipant(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getCurrentPoints() {
		return currentPoints;
	}

	public int getAdditionalPoints() {
		return additionalPoints;
	}

	public void setAdditionalPoints(int additionalPoints) {
		this.additionalPoints = additionalPoints;
	}

	public void increaseCurrentPoints(int points) {
		this.currentPoints += points;
	}

	@Override
	public int compareTo(DynamicQuestParticipant participant) {
		if (getCurrentPoints() + getAdditionalPoints() > participant.getCurrentPoints() + participant.getAdditionalPoints()) {
			return 1;
		} else if (getCurrentPoints() + getAdditionalPoints() > participant.getCurrentPoints() + participant.getAdditionalPoints()) {
			return 0;
		} else {
			return -1;
		}
	}
}
