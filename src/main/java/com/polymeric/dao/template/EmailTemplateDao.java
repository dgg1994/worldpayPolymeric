package com.polymeric.dao.template;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.polymeric.entity.template.EmailTemplateEntity;

@Repository
public interface EmailTemplateDao extends BaseMapper<EmailTemplateEntity>{

	@Select("select * from email_template where template_number = #{num} and language = #{language}")
	EmailTemplateEntity findByNum(@Param("num") Integer num,@Param("language") String language);

	@Select("<script>"
			+ "select * from email_template where 1=1"
			+ "<if test = 'templateNumber != null'> and template_number = #{templateNumber}</if>"
			+ "<if test = 'language != null'> and language = #{language}</if>"
			+ "</script>")
	List<EmailTemplateEntity> findList(EmailTemplateEntity entity);



}
