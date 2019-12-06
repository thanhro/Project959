package com.thanhld.server959.model.point;

import com.thanhld.server959.model.AbstractModel;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "point")
public class Point extends AbstractModel {
    String email;
    String name;
    String link;
    String point;

    public Point(String name, String email, String link, String point) {
        this.name = name;
        this.email = email;
        this.link = link;
        this.point = point;
    }

    public Point() {
    }
}
