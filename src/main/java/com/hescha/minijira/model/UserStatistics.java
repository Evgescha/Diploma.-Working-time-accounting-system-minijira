package com.hescha.minijira.model;

import lombok.Data;

import java.util.List;

@Data
public class UserStatistics {
    private User user;
    private long assignedIssuesCount;
    private long totalTimeSpent;
    private List<Issue> last5Issues;
    private List<Activity> last10TimeActivities;
}

