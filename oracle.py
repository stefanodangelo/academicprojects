from qiskit import QuantumCircuit
from .mcz import mcz

def oracle(qc, qr):
    
    qc.x(qr)
    qc = mcz(qc, qr)
    qc.x(qr)

    
    return qc