package com.polymeric.query.admin;

import com.polymeric.query.pub.PageQueryHelperEntity;
import lombok.Data;

/**
 * 类描述：首页查询参数
 *
 * @author GeminiSun
 * @date 2026/07/06 11:16
 */
@Data
public class StatQuery extends PageQueryHelperEntity {

    private String merchantAppId;

}