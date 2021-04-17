from Utils.load_data_2020 import load_URM, load_ICM
from CrossValidationWithBO.CreatigEvaluators import CrossKValidator
from KNN.ItemKNNCBFRecommender import ItemKNNCBFRecommender
from BO.bayes_opt import BayesianOptimization
import pandas as pd
import csv
import numpy as np
######### ----------------------------------------------------------------------------------------------------------


URM_all = load_URM("../Data/data_train.csv")
ICM_all = load_ICM("../Data/data_ICM_title_abstract.csv")

######### ----------------------------------------------------------------------------------------------------------
k = 10
cross_validator = CrossKValidator(URM_all=URM_all, k=k)
evaluator_list, URM_train_list, URM_validation_list = cross_validator.creating_k_evaluator()

######### ----------------------------------------------------------------------------------------------------------

tuning_params = {
    "TopK": (50, 3000),
    "shrink": (0, 3000)
}

######### ----------------------------------------------------------------------------------------------------------

def BO_func(TopK, shrink):

    cumulative_MAP = 0

    for i in range(k):
        recommender = ItemKNNCBFRecommender(URM_train_list[i], ICM_all)
        recommender.fit(topK=int(TopK), shrink=int(shrink), feature_weighting="TF-IDF", similarity="asymmetric")
        MAP = evaluator_list[i].evaluateRecommender(recommender)[0][10]["MAP"]
        cumulative_MAP += MAP

    return cumulative_MAP/k

######### ----------------------------------------------------------------------------------------------------------

optimizer = BayesianOptimization(
    f=BO_func,
    pbounds=tuning_params,
    verbose=5,
    random_state=30,
)

optimizer.maximize(
    init_points=30,
    n_iter=100,
)

