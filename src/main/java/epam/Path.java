package epam;

public final class Path {
    // pages
    public static final String PAGE__LOGIN = "/index.jsp";
    public static final String PAGE__ERROR_PAGE = "/jsp/error_page.jsp";
    public static final String PAGE__LIST_TOPICS = "/jsp/client/list_topics.jsp";
    public static final String PAGE__LIST_ELECTIVES= "/jsp/admin/list_electives.jsp";
    public static final String PAGE__LIST_JOURNAL = "/jsp/lecturer/journal.jsp";
    public static final String PAGE__SPORT_ELECTIVE = "/jsp/client/sport_elective.jsp";
    public static final String PAGE__PROGRAMMING_ELECTIVE = "/jsp/client/programming_elective.jsp";
    public static final String PAGE__HOME = "/jsp/client/cabinet.jsp";
    public static final String PAGE__CONTACT = "/jsp/contact.jsp";

    // commands
    public static final String COMMAND__LIST_TOPICS = "/controller?command=listTopics";
    public static final String COMMAND__LIST_ELECTIVES = "/controller?command=listElectives";
    public static final String COMMAND__LIST_JOURNAL = "/controller?command=journal";
}
