package com.example.fastlms.course.model;

import com.example.fastlms.admin.model.CommonParam;
import lombok.Data;

@Data
public class TakeCourseParam extends CommonParam {

    long id;
    String status;
    
    String userId;
    
    
    long searchCourseId;
}
