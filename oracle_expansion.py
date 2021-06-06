from zquantum.core.circuit import Circuit
from qiskit import QuantumCircuit, QuantumRegister
from convert import (to_zap,
                      from_json)
import json

def remove_x_gates(gates, mct_position, element, n_qubits):
    registers_without_gate = [i for i in range(len(element)) if element[i] == '1']
    gates_to_remove = []
    
    for index in registers_without_gate:
        gates_to_remove.append(mct_position + index + 4)
        gates_to_remove.append(mct_position - n_qubits - 3 + index)
    
    gates_to_remove.sort(reverse=True) # sort in order not to affect the positions of the gate to remove
    
    for i in gates_to_remove:
        del gates[i]

def adapt_oracle(circuit_path, element):
    # load circuit data
    with open(circuit_path, 'r') as f:
        metadata = json.load(f)
        
    # remove x gates where not needed 
    mct_positions = []
    mct_in_oracle = True # boolean telling whether the mct is in the oracle or not
    for i in range(len(metadata['gates'])):
        if (metadata['gates'][i]['name'] == 'MCT' or metadata['gates'][i]['name'] == 'CNOT' or metadata['gates'][i]['name'] == 'CCX'):
            if mct_in_oracle:
                mct_positions.append(i)
                mct_in_oracle = False
            else:
                mct_in_oracle = True
    
    mct_positions.sort(reverse=True) # sort in order not to affect the positions of the gate to remove
    
    for position in mct_positions:
        remove_x_gates(metadata['gates'], position, element, len(element))  
        
    # save modified .json
    with open(circuit_path, 'w') as f:
        json.dump(metadata, f)

def expand_oracle(circuit_path, element_to_search):
    element_to_search = element_to_search[::-1] # reverse the string in order to correctly apply the oracle function
    
    # oracle expansion
    adapt_oracle(circuit_path, element_to_search)