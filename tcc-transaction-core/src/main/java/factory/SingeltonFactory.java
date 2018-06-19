package factory;

public class SingeltonFactory<T> {

    private volatile T instance = null;

    private String className;

    public SingeltonFactory(){}

    public SingeltonFactory(Class<T> clazz, T instance) {
        this.className = clazz.getName();
        this.instance = instance;
    }

    public SingeltonFactory(Class<T> clazz) {
        this.className = clazz.getName();
    }

    public T getInstance() {

        //线程安全的单例工厂--双检查锁机制
        if (instance == null) {
            synchronized (SingeltonFactory.class) {
                if (instance == null) {

                    try {

                        ClassLoader loader = Thread.currentThread().getContextClassLoader();
                        Class<?> clazz = loader.loadClass(className);
                        instance = (T) clazz.newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to create an instance of " + className, e);
                    }
                }
            }
        }

        return instance;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        SingeltonFactory that = (SingeltonFactory) other;

        if (!className.equals(that.className)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return className.hashCode();
    }
}
