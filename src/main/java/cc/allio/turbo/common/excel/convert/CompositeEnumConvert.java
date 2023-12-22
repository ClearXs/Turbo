package cc.allio.turbo.common.excel.convert;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author h.x
 * @date 2023/12/27 9:57
 * @since 0.1.0
 */
@Component
public class CompositeEnumConvert implements ResourceLoaderAware {

    @Value("${excel.enum.scanner.package}")
    private String excelEnumScannerPackage;

    private ResourceLoader resourceLoader;

    private static List<ExcelEnumConvert> allEnumConvert = new ArrayList<>();

    private void initConvert() {
        ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        MetadataReaderFactory metaReader = new CachingMetadataReaderFactory(resourceLoader);
        Resource[] resources;
        try {
            resources = resolver.getResources("classpath*:" + excelEnumScannerPackage + ".class");
            Arrays.stream(resources).forEach(resource -> {
                        try {
                            String className = metaReader.getMetadataReader(resource).getClassMetadata().getClassName();
                            Class<?> clazz = Class.forName(className);
                            if (ExcelEnumConvert.isExcelEnums(clazz)) {
                                allEnumConvert.add(new ExcelEnumConvert<>((Class<Enum>) clazz));
                            }
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        initConvert();
    }

    public static List<ExcelEnumConvert> getExcelEnumConvert(){
        return allEnumConvert;
    }
}
