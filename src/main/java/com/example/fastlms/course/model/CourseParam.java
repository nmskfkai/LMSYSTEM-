package com.example.fastlms.course.model;

import com.example.fastlms.admin.model.CommonParam;
import lombok.Data;

@Data
public class CourseParam extends CommonParam {

    long id;//course.id
    long categoryId;

}
