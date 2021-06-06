from qiskit import QuantumCircuit

def init(qc, qr):
    qc.h(qr)
    
    return qc