package lyzzcw.work.zim.spi.factory;





import lyzzcw.work.zim.spi.annotation.SPI;
import lyzzcw.work.zim.spi.annotation.SPIClass;
import lyzzcw.work.zim.spi.loader.ExtensionLoader;

import java.util.Optional;

/**
 * SpiExtensionFactory
 */
@SPIClass
public class SpiExtensionFactory implements ExtensionFactory {
    @Override
    public <T> T getExtension(final String key, final Class<T> clazz) {
        return Optional.ofNullable(clazz)
                .filter(Class::isInterface)
                .filter(cls -> cls.isAnnotationPresent(SPI.class))
                .map(ExtensionLoader::getExtensionLoader)
                .map(ExtensionLoader::getDefaultSpiClassInstance)
                .orElse(null);
    }
}
