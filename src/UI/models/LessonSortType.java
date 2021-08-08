package UI.models;

public enum LessonSortType {
    DayTimeOriented("DayTimeOriented"),CLASS_ORIENTED("ClassOriented"), TEACHER_ORIENTED("TeacherOriented");

    public String name;
    LessonSortType(String name) {
        this.name = name;
    }

    public static LessonSortType valueOfLabel(String label) {
        for (LessonSortType e : values()) {
            if (e.name.equals(label)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
