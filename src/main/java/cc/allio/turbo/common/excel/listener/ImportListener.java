package cc.allio.turbo.common.excel.listener;

import cc.allio.turbo.common.db.mybatis.service.ITurboCrudService;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Excel监听器
 *
 * @author h.x
 * @date 2023/12/29 17:52
 * @since 0.1.0
 */
@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ImportListener<T> extends AnalysisEventListener<T> {

    /**
     * 默认每隔3000条存储数据库
     */
    private int batchCount = 3000;
    /**
     * 缓存的数据列表
     */
    private List<T> list = new ArrayList<>();
    /**
     * 数据导入类
     */
    private final ITurboCrudService importer;

    @Override
    public void invoke(T data, AnalysisContext analysisContext) {
        list.add(data);
        // 达到BATCH_COUNT，则调用importer方法入库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= batchCount) {
            // 调用importer方法
            importer.saveBatch(list);
            // 存储完成清理list
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 调用importer方法
        importer.save(list);
        // 存储完成清理list
        list.clear();
    }

}
