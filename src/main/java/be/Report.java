package be;

public class Report {
    private final int id;
    private final String title;
    private final String status;

    //  Pattern  Builder
    private Report(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.status = builder.status;
    }

    // Getters only – class is immutable
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        return title + " [" + status + "]";
    }

    // ---- Builder pattern ----
    public static class Builder {
        private int id;
        private String title;
        private String status;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setStatus(String status) {
            this.status = status;
            return this;
        }

        public Report build() {
            return new Report(this);
        }
    }
}

