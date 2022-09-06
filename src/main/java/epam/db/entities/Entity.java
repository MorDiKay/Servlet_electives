package epam.db.entities;


/**
 * Root of all entities which have identifier field(id).
 *
 */
abstract public class Entity {

    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
