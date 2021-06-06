
#from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
from math import sqrt
import json

def build_circuit(n_qubits, save_path = 'circuit.json'):
    message = "Welcome to Orquestra!"

    message_dict = {}
    message_dict["message"] = message
    message_dict["schema"] = "message"

    with open(save_path,'w') as f:
        f.write(json.dumps(message_dict, indent=2)) # Write message to file as this will serve as output artifact
    """
    # Define registers
    qr = QuantumRegister(n_qubits)
    cr = ClassicalRegister(n_qubits)
    
    # Create circuit
    qc = QuantumCircuit(qr, cr)
    
    # Build circuit
    qc = init(qc, qr)
    for i in range(round(sqrt(n_qubits))):
        qc = oracle(qc, qr) 
        qc.barrier(qr)
        qc.barrier(qr)
        qc = diffuser(qc, qr)
        qc.barrier(qr)
    qc = add_measurements(qc, qr, cr)
    
    to_zap(qc, save_path)
    """