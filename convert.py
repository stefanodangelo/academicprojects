from zquantum.core.circuit import save_circuit
from _circuit import Circuit
import json

def load_circuit(file):
    """Loads a circuit from a file.
    Args:
        file (str or file-like object): the name of the file, or a file-like object.
    Returns:
        circuit (core.Circuit): the circuit
    """

    if isinstance(file, str):
        with open(file, "r") as f:
            data = json.load(f)
    else:
        data = json.load(file)

    return Circuit.from_dict(data)

def to_zap(circuit, save_path):
    converted_circuit = Circuit(circuit)
    save_circuit(converted_circuit, save_path)
    
def from_json(load_path):
    circuit = load_circuit(load_path)
    return circuit.to_qiskit() 