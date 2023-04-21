package crosschain.fabric.contract.state;

@FunctionalInterface
public interface StateDeserializer {
    State deserialize(byte[] buffer);
}
