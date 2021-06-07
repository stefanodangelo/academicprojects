#from qiskit import QuantumCircuit, QuantumRegister, ClassicalRegister
#from math import sqrt
#from zquantum.core.circuit import save_circuit


#import pyquil
#import cirq
#import random
#import warnings
#import numpy as np

#from zquantum.core.circuit import Qubit, Circuit
#from zquantum.core.circuit._gateset import ALL_GATES
#from qiskit.circuit.quantumregister import Qubit as QiskitQubit
#from qiskit.circuit.classicalregister import Clbit as QiskitClbit
#from qiskit.circuit.library.standard_gates.x import MCXGrayCode

#from qiskit import IBMQ, Aer, assemble, transpile
#from qiskit.providers.ibmq import least_busy
#from qiskit.tools.monitor import job_monitor
import json


def setup(n_qubits, save_path = 'circuit.json'):
    
    message = "Welcome to Orquestra!"

    message_dict = {}
    message_dict["message"] = message
    message_dict["schema"] = "message"

    with open(save_path,'w') as f:
        f.write(json.dumps(message_dict, indent=2)) # Write message to file as this will serve as output artifact