package UI.TimeTable;

import UI.TimeTable.Components.Grade;
import UI.TimeTable.Rules.Rules;

import java.util.Set;

public class Table {
    Grade grade;
    Placement square;
    Set<Rules> applicableRules;
    int totalHours, totalDays;
}
