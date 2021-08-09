package UI.models;

import UI.models.Lesson;
import UI.models.LessonSortType;

import java.util.Comparator;

public class LessonComparator implements Comparator<Lesson> {

    LessonSortType sortType;
    public LessonComparator(LessonSortType sortType) {
        this.sortType = sortType;
    }

    @Override
    public int compare(Lesson lesson1, Lesson lesson2) {
        int compare = 0;
        switch (sortType) {
            case DayTimeOriented:
                compare = lesson1.getDay() - lesson2.getDay();
                if (compare == 0) {
                    compare = lesson1.getHour() - lesson2.getHour();
                    if (compare == 0) {
                        compare = lesson1.getClassId() - lesson2.getClassId();
                        if (compare == 0) {
                            compare = lesson1.getTeacherId() - lesson2.getTeacherId();
                        }
                    }
                }
                break;

            case CLASS_ORIENTED:
                compare = lesson1.getClassId() - lesson2.getClassId();
                break;

            case TEACHER_ORIENTED:
                compare = lesson1.getTeacherId() - lesson2.getTeacherId();
                break;

            default:
                break;
        }

        return compare;
    }
}
