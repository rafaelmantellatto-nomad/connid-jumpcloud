package com.jumpcloud.util;

import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.filter.EqualsFilter;
import org.identityconnectors.framework.common.objects.filter.Filter;
import org.identityconnectors.framework.spi.FilterTranslator;
import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;

public class JumpCloudFilterTranslator implements FilterTranslator<JumpCloudFilter> {

    @Override
    public JumpCloudFilter translate(Filter filter) {
       if (filter instanceof EqualsFilter) {
            EqualsFilter equalsFilter = (EqualsFilter) filter;
            Attribute attribute = equalsFilter.getAttribute();
            return new JumpCloudFilter(attribute.getName(), attribute.getValue().get(0));
         } else if (filter instanceof Uid.Equals) {
            Uid.Equals equalsFilter = (Uid.Equals) filter;
            return new JumpCloudFilter(Uid.NAME, equalsFilter.getUidValue());
        } else if (filter instanceof Name.Equals) {
            Name.Equals equalsFilter = (Name.Equals) filter;
            return new JumpCloudFilter(Name.NAME, equalsFilter.getNameValue());
        }

        return null;
    }
}