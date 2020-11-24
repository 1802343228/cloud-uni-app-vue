package com.soft1851.pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author crq
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveFriendLinkBO {
    private String id;
    private Integer isDelete;
    private String linkName;
    private String linkUrl;
}
