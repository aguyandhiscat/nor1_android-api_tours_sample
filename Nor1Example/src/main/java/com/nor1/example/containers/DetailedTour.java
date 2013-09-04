package com.nor1.example.containers;

import java.util.List;

/**
 * Created by alexwilczewski on 8/30/13.
 */
public class DetailedTour {
    public String name;
    public String descriptionLong;

    public List<ImageItem> images;
    public List<ScheduleItem> schedules;

    public class ImageItem {
        public String imageUrl;
        public String imageTitle;
    }

    public class ScheduleItem {
        public String id;
        public String title;
        public String type;
        public String description;
        public String commentary;
        public String from;
        public String to;
        public String day_hash;
        public String first_service;
        public String last_service;
        public String interval;
        public String departure_point;
        public String time;
        public String duration;
        public String duration_minutes;
        public String frequency;
        public String pickup_required;
        public String dropoff_required;
    }
}
