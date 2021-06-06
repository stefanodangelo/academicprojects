from .convert import to_zap
from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
from .init import init
from .oracle import oracle
from .diffuser import diffuser
from .measure import add_measurements
from math import sqrt

def build_circuit(n_qubits, save_path = 'circuit.json'):

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