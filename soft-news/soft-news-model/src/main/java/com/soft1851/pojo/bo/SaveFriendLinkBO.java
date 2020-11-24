package com.soft1851.pojo.bo;

import com.soft1851.validate.CheckUrl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author crq
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveFriendLinkBO {
    private String id;
    @NotNull(message = "请选择保留或删除")
    private Integer isDelete;
    @NotBlank(message = "友情链接不能为空")
    private String linkName;
    @NotBlank(message = "友情链接地址不能为空")
    @CheckUrl
    private String linkUrl;
}
