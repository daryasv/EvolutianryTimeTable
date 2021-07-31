package UI.TimeTableModels;

import UI.TimeTableModels.Components.Grade;
import UI.TimeTableModels.Rules.Rules;

import java.util.Set;

public class Table {
    Grade grade;
    Placement square;
    Set<Rules> applicableRules;
    int totalHours, totalDays;
}
