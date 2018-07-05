package org.tis.senior.module.core.entity.cascade;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.tis.senior.module.core.web.util.SpringUtil;
import org.tis.senior.module.developer.entity.SDelivery;
import org.tis.senior.module.developer.entity.SProfiles;
import org.tis.senior.module.developer.entity.SWorkitem;
import org.tis.senior.module.developer.service.ISProfilesService;
import org.tis.senior.module.developer.service.ISWorkitemService;
import org.tis.senior.module.developer.util.DeveloperUtils;

import java.io.IOException;
import java.lang.reflect.Type;

public class CommonCascadeSerializer implements ObjectSerializer {


    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {

        String name = fieldName.toString();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("source",object);
        if(name.equals(DeveloperUtils.UnderlineToHump(SDelivery.COLUMN_GUID_PROFILES))){
            ISProfilesService profilesService = SpringUtil.getBean(ISProfilesService.class);
            SProfiles profiles = profilesService.selectOneById(object.toString());
            jsonObject.put("target",profiles.getProfilesName());
        }else if(name.equals(DeveloperUtils.UnderlineToHump(SDelivery.COLUMN_GUID_WORKITEM))){
            ISWorkitemService workitemService = SpringUtil.getBean(ISWorkitemService.class);
            SWorkitem workitem = workitemService.selectOneById(object.toString());
            jsonObject.put("target",workitem.getItemName());
        }
        serializer.write(jsonObject);
    }

}
