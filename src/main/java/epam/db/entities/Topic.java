package epam.db.entities;

/**
* Topic entity (topics)
*
*/
public enum Topic  {
    SPORT, PROGRAMMING;

    public static Topic getTopic(Elective elective) {
        int topicId = elective.getTopicId();
        return Topic.values()[topicId];
    }

    public String getName() { return name().toLowerCase(); }
}
