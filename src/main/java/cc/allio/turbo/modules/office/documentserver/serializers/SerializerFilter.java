package cc.allio.turbo.modules.office.documentserver.serializers;

import java.util.List;

public class SerializerFilter {
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof List) {
            return ((List<?>) obj).size() == 1 && ((List<?>) obj).get(0) == FilterState.NULL.toString();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
