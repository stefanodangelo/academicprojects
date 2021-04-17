from Base.BaseSimilarityMatrixRecommender import BaseItemSimilarityMatrixRecommender
from Base.Recommender_utils import check_matrix

class ItemKNNScoresHybridRecommender(BaseItemSimilarityMatrixRecommender):
    """ ItemKNNScoresHybridRecommender
    Hybrid of two prediction scores R = R1*alpha + R2*beta + R3*gamma + R4*delta + R5*(1-(alpha+beta+gamma+delta))

    """

    RECOMMENDER_NAME = "ItemKNNScoresHybridRecommender"


    def __init__(self, URM_train, recommenders_list):
        super(ItemKNNScoresHybridRecommender, self).__init__(URM_train)

        self.URM_train = check_matrix(URM_train.copy(), 'csr')
        self.recommenders = recommenders_list

        
    def fit(self, coefficients):
        assert len(coefficients) == (len(self.recommenders) - 1), "Unproper number of coefficients"
        
        last_coefficient = 1.0
        for coefficient in coefficients:
            last_coefficient -= coefficient
            
        coefficients.append(last_coefficient)
        
        self.coefficients = coefficients
        
    def _compute_item_score(self, user_id_array, items_to_compute):
        
        item_weights = []
        for recommender in self.recommenders:
            item_weights.append(recommender._compute_item_score(user_id_array, None)) 
        
        weighted_score = 0
        for i in range(len(item_weights)):
            weighted_score += item_weights[i]*self.coefficients[i]

        return weighted_score