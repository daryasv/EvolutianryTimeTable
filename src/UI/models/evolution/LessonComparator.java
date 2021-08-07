package UI.models.evolution;

import UI.models.Lesson;

import java.util.Comparator;

public class LessonComparator implements Comparator<Lesson> {

    CrossoverSortType sortType;
    public LessonComparator(CrossoverSortType sortType) {
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
            default:
                break;
        }

        return compare;
    }
}
