package cc.allio.turbo.common.event;

public class Subscribers {


    public static <D> DelegateSubscriber<D> from(Subscriber<D> domain) {
        return new DelegateSubscriber<>(domain);
    }


    public static class DelegateSubscriber<D> implements Subscriber<D> {

        Subscriber<D> original;

        public DelegateSubscriber(Subscriber<D> original) {
            this.original = original;
        }

        @Override
        public void setDomainEventBus(DomainEventBus eventBus) {
            this.original.setDomainEventBus(eventBus);
        }

        @Override
        public DomainEventBus getDomainEventBus() {
            return original.getDomainEventBus();
        }
    }
}
