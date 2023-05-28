package com.delgo.reward.mongoDomain;

import com.delgo.reward.comm.code.DetailCode;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="detail_data")
public class MungpleDetailData {
    @Id
    private String id;
    @Field("mungple_id")
    private int mungpleId;
    @Field("business_hour")
    private Map<String, String> businessHour;
    @Field("resident_dog_name")
    private String residentDogName;
    @Field("represent_site")
    private String representSite;
    @Field("represent_menu_title")
    private Map<String, String> representMenuTitle;
    @Field("accept_size")
    private Map<String, DetailCode> acceptSize;
    @Field("enter_desc")
    private String enterDesc;

    @Field("represent_menu_photo_url")
    private List<String> representMenuPhotoUrl;
    @Field("parking_limit")
    private int parkingLimit;

    @Field("editor_note_url")
    private String editorNoteUrl;

    @Field("copy_link")
    private String copyLink;
}
