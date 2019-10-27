package de.bright_side.lgf.model;

public class LResourceId implements Comparable<Object>{
    private String id;

    public LResourceId(String id){
        this.id = id;
    }

    public String name(){
        return id;
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof LResourceId)){
            return 1;
        }
        LResourceId r = (LResourceId)o;

        if ((o == null) || (r.id == null)){
            return 1;
        }
        if (id == null){
            return -1;
        }
        return id.compareTo(r.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!(o instanceof LResourceId)){
            return false;
        }
        LResourceId that = (LResourceId) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "" + id;
    }
}
