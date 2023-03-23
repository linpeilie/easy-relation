package cn.easii.relation.core.bean;

import cn.easii.relation.CacheStrategy;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 针对单个关联项的描述类
 */
@Getter
@ToString
public class RelationMeta implements Serializable {

    public RelationMeta(String dataProvider) {
        this.dataProvider = dataProvider;
        this.items = new ArrayList<>();
    }

    private String dataProvider;

    @Setter
    private CacheStrategy cacheStrategy;

    private List<RelationItemMeta> items;

    public boolean addItem(RelationItemMeta item) {
        return items.add(item);
    }

}
