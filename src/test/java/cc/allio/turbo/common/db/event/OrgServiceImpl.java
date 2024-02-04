package cc.allio.turbo.common.db.event;

import cc.allio.turbo.common.db.entity.Org;

public class OrgServiceImpl implements Subscriber<Org> {

    public String getName() {
        return "org";
    }

}
