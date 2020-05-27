#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define RED   'R'
#define BLACK 'B'
#define TRUE 't'
#define FALSE 'f'

//nodo dell'RBT che monitora tutte le entita'
typedef struct nodo_string {
    char*               id;
    char                color;
    struct nodo_string* right;
    struct nodo_string* left;
    struct nodo_string* p;
} Nodo_String;

//nodo dell'RBT per il report
typedef struct nodo_report {
    char**              id;
    char                color;
    struct nodo_report* right;
    struct nodo_report* left;
    struct nodo_report* p;
} Nodo_Report;

//nodo dell'RBT delle entita' mittenti una relazione (e anche dell'RBT per la monitorazione delle entita')
typedef struct nodo_ent {
    char**           id;
    char             color;
    struct nodo_ent* right;
    struct nodo_ent* left;
    struct nodo_ent* p;
} Nodo_Ent;

//nodo dell'RBT delle entita' riceventi la relazione
typedef struct nodo_dest {
    char**            id;
    char              color;
    int               num_of_rel; //contatore per il numero di relazioni entranti
    struct nodo_dest* right;
    struct nodo_dest* left;
    struct nodo_dest* p;
    struct nodo_ent*  deep;	//puntatore alla radice dell'RBT dei mittenti
} Nodo_Dest;

//nodo dell'RBT delle relazioni
typedef struct nodo_rel {
    char*             id;
    char              color;
    struct nodo_rel*  left;
    struct nodo_rel*  right;
    struct nodo_rel*  p;
    struct nodo_dest* deep;	//puntatore alla radice dell'RBT dei destinatari
    struct nodo_dest* max;  //puntatore all'elemento col maggior numero di relazioni entranti
    struct nodo_report* report;	//puntatore alla radice dell'RBT per il report
} Nodo_Rel;

//dichiarazione dei nodi NIL per ogni RBT
struct nodo_rel T_Nil_Node_Rel; 
Nodo_Rel* T_Nil_Rel = &T_Nil_Node_Rel;  
struct nodo_dest T_Nil_Node_Dest;  
Nodo_Dest* T_Nil_Dest = &T_Nil_Node_Dest; 
struct nodo_ent T_Nil_Node_Ent;  
Nodo_Ent* T_Nil_Ent = &T_Nil_Node_Ent;  
struct nodo_report T_Nil_Node_Report;
Nodo_Report* T_Nil_Report = &T_Nil_Node_Report;	
struct nodo_string T_Nil_Node_String;	
Nodo_String* T_Nil_String = &T_Nil_Node_String;	

//dichiarazione delle radici degli unici due RBT "accessibili dall'esterno"
Nodo_Rel* Root_Rel = NULL;
Nodo_String* Root_String = NULL;

//flag utile per la stampa nei report
char flag = TRUE;

//crea un nuovo nodo per l'RBT delle relazioni
Nodo_Rel* newNodo_Rel(char* id){
    Nodo_Rel* new = (Nodo_Rel*) malloc(sizeof(Nodo_Rel));
    new->id = (char*)malloc(sizeof(char)*(strlen(id)+1));
    strncpy(new->id, id, strlen(id)+1);
    new->color = RED;
    new->left = NULL;
    new->right = NULL;
    new->p = NULL;
    new->deep = NULL;
    new->max = NULL;
    new->report = NULL;

    return new;
}

//crea un nuovo nodo per l'RBT delle entita' destinatarie
Nodo_Dest* newNodoDest(char** id){
    Nodo_Dest* new = (Nodo_Dest*)malloc(sizeof(Nodo_Dest));
    new->id = id;
    new-> color = RED;
    new-> num_of_rel = 0;
    new-> right = NULL;
    new-> left = NULL;
    new-> p = NULL;
    new-> deep = NULL;

    return new;
}

//crea un nuovo nodo per l'RBT delle entita' mittenti
Nodo_Ent* newNodoEnt(char** id){
    Nodo_Ent* new = (Nodo_Ent*)malloc(sizeof(Nodo_Ent));
    new->id = id;
    new->color = RED;
    new->right = NULL;
    new->left = NULL;
    new->p = NULL;
    return new;
}

//crea un nuovo nodo per l'RBT del report
Nodo_Report* newNodoReport(char** id){
    Nodo_Report* new = (Nodo_Report*)malloc(sizeof(Nodo_Report));
    new->id = id;
    new-> color = RED;
    new-> right = NULL;
    new-> left = NULL;
    new-> p = NULL;

    return new;
}

//crea un nuovo nodo per l'RBT che monitora tutte le entita'
Nodo_String* newNodoString(char* id){
    Nodo_String* new = (Nodo_String*)malloc(sizeof(Nodo_String));
    new->id = (char*)malloc(sizeof(char)*(strlen(id)+1));
    strncpy(new->id, id, strlen(id)+1);
    new->color = RED;
    new->right = NULL;
    new->left = NULL;
    new->p = NULL;
    return new;
}

//rotazione sinistra dell'albero delle relazioni
void leftRotate_Rel(Nodo_Rel** T, Nodo_Rel* x){
    Nodo_Rel *y  = x->right;    //inizializzo y
    x->right = y->left;     //ruoto il sottoalbero sx di y nel sottoalbero dx di x
    if (y->left != T_Nil_Rel)
        y->left->p = x;
    y->p = x->p;  //collego il padre di y al padre di x
    if (x->p == T_Nil_Rel)
        *T = y;
    else if (x == x->p->left)
        x->p->left = y;
    else
        x->p->right = y;
    y->left = x;    //sposto x alla sinistra di y
    x->p = y;
}

//rotazione sinistra dell'albero delle entita' riceventi
void leftRotate_Dest(Nodo_Dest** T, Nodo_Dest* x){
    Nodo_Dest *y  = x->right;    //inizializzo y
    x->right = y->left;     //ruoto il sottoalbero sx di y nel sottoalbero dx di x
    if (y->left != T_Nil_Dest)
        y->left->p = x;
    y->p = x->p;  //collego il padre di y al padre di x
    if (x->p == T_Nil_Dest)
        *T = y;
    else if (x == x->p->left)
        x->p->left = y;
    else
        x->p->right = y;
    y->left = x;    //sposto x alla sinistra di y
    x->p = y;
}

//rotazione sinistra dell'albero delle entita' mittenti
void leftRotate_Ent(Nodo_Ent** T, Nodo_Ent* x){
    Nodo_Ent *y  = x->right;    //inizializzo y
    x->right = y->left;     //ruoto il sottoalbero sx di y nel sottoalbero dx di x
    if (y->left != T_Nil_Ent)
        y->left->p = x;
    y->p = x->p;  //collego il padre di y al padre di x
    if (x->p == T_Nil_Ent)
        *T = y;
    else if (x == x->p->left)
        x->p->left = y;
    else
        x->p->right = y;
    y->left = x;    //sposto x alla sinistra di y
    x->p = y;
}

//rotazione sinistra dell'albero del report
void leftRotate_Report(Nodo_Report** T, Nodo_Report* x){
    Nodo_Report *y  = x->right;    //inizializzo y
    x->right = y->left;     //ruoto il sottoalbero sx di y nel sottoalbero dx di x
    if (y->left != T_Nil_Report)
        y->left->p = x;
    y->p = x->p;  //collego il padre di y al padre di x
    if (x->p == T_Nil_Report)
        *T = y;
    else if (x == x->p->left)
        x->p->left = y;
    else
        x->p->right = y;
    y->left = x;    //sposto x alla sinistra di y
    x->p = y;
}

//rotazione sinistra dell'albero che monitora tutte le entita'
void leftRotate_String(Nodo_String** T, Nodo_String* x){
    Nodo_String *y  = x->right;    //inizializzo y
    x->right = y->left;     //ruoto il sottoalbero sx di y nel sottoalbero dx di x
    if (y->left != T_Nil_String)
        y->left->p = x;
    y->p = x->p;  //collego il padre di y al padre di x
    if (x->p == T_Nil_String)
        *T = y;
    else if (x == x->p->left)
        x->p->left = y;
    else
        x->p->right = y;
    y->left = x;    //sposto x alla sinistra di y
    x->p = y;
}

//rotazione destra dell'albero delle relazioni
void rightRotate_Rel(Nodo_Rel** T, Nodo_Rel* y){
    Nodo_Rel *x  = y->left;     //inizializzo x
    y->left = x->right;    //ruoto il sottoalbero dx di x nel sottoalbero sx di y
    if (x->right != T_Nil_Rel)
        x->right->p = y;
    x->p = y->p;  //collego il padre di x al padre di y
    if (y->p == T_Nil_Rel)
        *T = x;
    else if (y == y->p->right)
        y->p->right = x;
    else
        y->p->left = x;
    x->right = y;   //sposto y alla destra di x
    y->p = x;
}

//rotazione destra dell'albero delle entita' riceventi
void rightRotate_Dest(Nodo_Dest** T, Nodo_Dest* y){
    Nodo_Dest *x  = y->left;     //inizializzo x
    y->left = x->right;    //ruoto il sottoalbero dx di x nel sottoalbero sx di y
    if (x->right != T_Nil_Dest)
        x->right->p = y;
    x->p = y->p;  //collego il padre di x al padre di y
    if (y->p == T_Nil_Dest)
        *T = x;
    else if (y == y->p->right)
        y->p->right = x;
    else
        y->p->left = x;
    x->right = y;   //sposto y alla destra di x
    y->p = x;
}

//rotazione destra dell'albero delle entita' mittenti
void rightRotate_Ent(Nodo_Ent** T, Nodo_Ent* y){
    Nodo_Ent *x  = y->left;     //inizializzo x
    y->left = x->right;    //ruoto il sottoalbero dx di x nel sottoalbero sx di y
    if (x->right != T_Nil_Ent)
        x->right->p = y;
    x->p = y->p;  //collego il padre di x al padre di y
    if (y->p == T_Nil_Ent)
        *T = x;
    else if (y == y->p->right)
        y->p->right = x;
    else
        y->p->left = x;
    x->right = y;   //sposto y alla destra di x
    y->p = x;
}

//rotazione destra dell'albero del report
void rightRotate_Report(Nodo_Report** T, Nodo_Report* y){
    Nodo_Report *x  = y->left;     //inizializzo x
    y->left = x->right;    //ruoto il sottoalbero dx di x nel sottoalbero sx di y
    if (x->right != T_Nil_Report)
        x->right->p = y;
    x->p = y->p;  //collego il padre di x al padre di y
    if (y->p == T_Nil_Report)
        *T = x;
    else if (y == y->p->right)
        y->p->right = x;
    else
        y->p->left = x;
    x->right = y;   //sposto y alla destra di x
    y->p = x;
}

//rotazione destra dell'albero che monitora tutte le entita'
void rightRotate_String(Nodo_String** T, Nodo_String* y){
    Nodo_String *x  = y->left;     //inizializzo x
    y->left = x->right;    //ruoto il sottoalbero dx di x nel sottoalbero sx di y
    if (x->right != T_Nil_String)
        x->right->p = y;
    x->p = y->p;  //collego il padre di x al padre di y
    if (y->p == T_Nil_String)
        *T = x;
    else if (y == y->p->right)
        y->p->right = x;
    else
        y->p->left = x;
    x->right = y;   //sposto y alla destra di x
    y->p = x;
}

//procedura che ripristina le proprieta' dell'RBT delle relazioni a seguito di un inserimento
void RBInsertFixup_Rel(Nodo_Rel** Root, Nodo_Rel* z){
    Nodo_Rel* y;
    while (z != *Root && z->p->color == RED){
        if (z->p == z->p->p->left){
            y = z->p->p->right;
            if (y->color == RED){
                z->p->color = BLACK;
                y->color = BLACK;
                z->p->p->color = RED;
                z = z->p->p;
            }
            else {
                if (z == z->p->right){
                    z = z->p;
                    leftRotate_Rel(Root, z);
                }
                z->p->color = BLACK;
                z->p->p->color = RED;
                rightRotate_Rel(Root, z->p->p);
            }
        }
        else{
            y = z->p->p->left;
            if (y->color == RED){
                z->p->color = BLACK;
                y->color = BLACK;
                z->p->p->color = RED;
                z = z->p->p;
            }
            else {
                if (z == z->p->left){
                    z = z->p;
                    rightRotate_Rel(Root, z);
                }
                z->p->color = BLACK;
                z->p->p->color = RED;
                leftRotate_Rel(Root, z->p->p);
            }
        }
    }
    (*Root)->color = BLACK;
}

//procedura che ripristina le proprieta' dell'RBT delle entita' riceventi a seguito di un inserimento
void RBInsertFixup_Dest(Nodo_Dest** Root, Nodo_Dest* z){
    Nodo_Dest* y;
    while (z != *Root && z->p->color == RED){
        if (z->p == z->p->p->left){
            y = z->p->p->right;
            if (y->color == RED){
                z->p->color = BLACK;
                y->color = BLACK;
                z->p->p->color = RED;
                z = z->p->p;
            }
            else {
                if (z == z->p->right){
                    z = z->p;
                    leftRotate_Dest(Root, z);
                }
                z->p->color = BLACK;
                z->p->p->color = RED;
                rightRotate_Dest(Root, z->p->p);
            }
        }
        else{
            y = z->p->p->left;
            if (y->color == RED){
                z->p->color = BLACK;
                y->color = BLACK;
                z->p->p->color = RED;
                z = z->p->p;
            }
            else {
                if (z == z->p->left){
                    z = z->p;
                    rightRotate_Dest(Root, z);
                }
                z->p->color = BLACK;
                z->p->p->color = RED;
                leftRotate_Dest(Root, z->p->p);
            }
        }
    }
    (*Root)->color = BLACK;
}

//procedura che ripristina le proprieta' dell'RBT delle entita' mittenti a seguito di un inserimento
void RBInsertFixup_Ent(Nodo_Ent** Root, Nodo_Ent* z){
    Nodo_Ent* y;
    while (z != *Root && z->p->color == RED){
        if (z->p == z->p->p->left){
            y = z->p->p->right;
            if (y->color == RED){
                z->p->color = BLACK;
                y->color = BLACK;
                z->p->p->color = RED;
                z = z->p->p;
            }
            else {
                if (z == z->p->right){
                    z = z->p;
                    leftRotate_Ent(Root, z);
                }
                z->p->color = BLACK;
                z->p->p->color = RED;
                rightRotate_Ent(Root, z->p->p);
            }
        }
        else{
            y = z->p->p->left;
            if (y->color == RED){
                z->p->color = BLACK;
                y->color = BLACK;
                z->p->p->color = RED;
                z = z->p->p;
            }
            else {
                if (z == z->p->left){
                    z = z->p;
                    rightRotate_Ent(Root, z);
                }
                z->p->color = BLACK;
                z->p->p->color = RED;
                leftRotate_Ent(Root, z->p->p);
            }
        }
    }
    (*Root)->color = BLACK;
}

//procedura che ripristina le proprieta' dell'RBT per il report a seguito di un inserimento
void RBInsertFixup_Report(Nodo_Report** Root, Nodo_Report* z){
    Nodo_Report* y;
    while (z != *Root && z->p->color == RED){
        if (z->p == z->p->p->left){
            y = z->p->p->right;
            if (y->color == RED){
                z->p->color = BLACK;
                y->color = BLACK;
                z->p->p->color = RED;
                z = z->p->p;
            }
            else {
                if (z == z->p->right){
                    z = z->p;
                    leftRotate_Report(Root, z);
                }
                z->p->color = BLACK;
                z->p->p->color = RED;
                rightRotate_Report(Root, z->p->p);
            }
        }
        else{
            y = z->p->p->left;
            if (y->color == RED){
                z->p->color = BLACK;
                y->color = BLACK;
                z->p->p->color = RED;
                z = z->p->p;
            }
            else {
                if (z == z->p->left){
                    z = z->p;
                    rightRotate_Report(Root, z);
                }
                z->p->color = BLACK;
                z->p->p->color = RED;
                leftRotate_Report(Root, z->p->p);
            }
        }
    }
    (*Root)->color = BLACK;
}

//procedura che ripristina le proprieta' dell'RBT che monitora tutte le entita' a seguito di un inserimento
void RBInsertFixup_String(Nodo_String** Root, Nodo_String* z){
    Nodo_String* y;
    while (z != *Root && z->p->color == RED){
        if (z->p == z->p->p->left){
            y = z->p->p->right;
            if (y->color == RED){
                z->p->color = BLACK;
                y->color = BLACK;
                z->p->p->color = RED;
                z = z->p->p;
            }
            else {
                if (z == z->p->right){
                    z = z->p;
                    leftRotate_String(Root, z);
                }
                z->p->color = BLACK;
                z->p->p->color = RED;
                rightRotate_String(Root, z->p->p);
            }
        }
        else{
            y = z->p->p->left;
            if (y->color == RED){
                z->p->color = BLACK;
                y->color = BLACK;
                z->p->p->color = RED;
                z = z->p->p;
            }
            else {
                if (z == z->p->left){
                    z = z->p;
                    rightRotate_String(Root, z);
                }
                z->p->color = BLACK;
                z->p->p->color = RED;
                leftRotate_String(Root, z->p->p);
            }
        }
    }
    (*Root)->color = BLACK;
}

//procedura di inserimento di un nodo nell'RBT delle relazioni
Nodo_Rel* RBInsert_Rel(Nodo_Rel** T, char* id){
    Nodo_Rel* z =  newNodo_Rel(id);
    Nodo_Rel* y =  T_Nil_Rel;
    Nodo_Rel* x = *T;
    //trovo la posizione in cui inserire z nel BST
    while (x != T_Nil_Rel) {
        y = x;
        if (strcmp(z->id, x->id)<0)
            x = x->left;
        else
            x = x->right;
    }
    z->p = y;
    if (y == T_Nil_Rel)
        *T = z;
    else if (strcmp(z->id, y->id)<0)
        y->left  = z;
    else
        y->right = z;
    //inizializzo z come nodo rosso
    z->left  = T_Nil_Rel;
    z->right = T_Nil_Rel;
    z->deep = T_Nil_Dest;
    z->max = T_Nil_Dest;
    z->report = T_Nil_Report;
    z->color = RED;
    RBInsertFixup_Rel(T, z);

    return z;
}

//procedura di inserimento di un nodo nell'RBT delle entita' destinatarie
Nodo_Dest* RBInsert_Dest(Nodo_Dest** T, char** id){
    Nodo_Dest* z =  newNodoDest(id);
    Nodo_Dest* y =  T_Nil_Dest;
    Nodo_Dest* x = *T;
    //trovo la posizione in cui inserire z nel BST
    while (x != T_Nil_Dest){
        y = x;
        if (strcmp(*z->id, *x->id)<0)
            x = x->left;
        else
            x = x->right;
    }
    z->p = y;
    if (y == T_Nil_Dest)
        *T = z;
    else if (strcmp(*z->id, *y->id)<0)
        y->left  = z;
    else
        y->right = z;
    //inizializzo z come nodo rosso
    z->left  = T_Nil_Dest;
    z->right = T_Nil_Dest;
    z->deep = T_Nil_Ent;
    z->num_of_rel = 0;
    z->color = RED;
    RBInsertFixup_Dest(T, z);

    return z;
}

//procedura di inserimento di un nodo nell'RBT del report
Nodo_Report* RBInsert_Report(Nodo_Report** T, char** id){
    Nodo_Report* z =  newNodoReport(id);
    Nodo_Report* y =  T_Nil_Report;
    Nodo_Report* x = *T;
    //trovo la posizione in cui inserire z nel BST
    while (x != T_Nil_Report) {
        y = x;
        if (strcmp(*z->id, *x->id)<0)
            x = x->left;
        else
            x = x->right;
    }
    z->p = y;
    if (y == T_Nil_Report)
        *T = z;
    else if (strcmp(*z->id, *y->id)<0)
        y->left  = z;
    else
        y->right = z;
    //inizializzo z come nodo rosso
    z->left  = T_Nil_Report;
    z->right = T_Nil_Report;
    z->color = RED;
    RBInsertFixup_Report(T, z);
    return z;
}

//procedura di inserimento di un nodo nell'RBT delle entita' mittenti
Nodo_Ent* RBInsert_Ent(Nodo_Ent** T, char** id){
    Nodo_Ent* z =  newNodoEnt(id);
    Nodo_Ent* y =  T_Nil_Ent;
    Nodo_Ent* x = *T;
    //trovo la posizione in cui inserire z nel BST
    while (x != T_Nil_Ent) {
        y = x;
        if (strcmp(*z->id, *x->id)<0)
            x = x->left;
        else
            x = x->right;
    }
    z->p = y;
    if (y == T_Nil_Ent)
        *T = z;
    else if (strcmp(*z->id, *y->id)<0)
        y->left = z;
    else
        y->right = z;
    //inizializzo z come nodo rosso
    z->left  = T_Nil_Ent;
    z->right = T_Nil_Ent;
    z->color = RED;
    RBInsertFixup_Ent(T, z);

    return z;
}

//procedura di inserimento di un nodo nell'RBT che monitora tutte le entita'
char** RBInsert_String(Nodo_String** T, char* id){
    Nodo_String* z =  newNodoString(id);
    Nodo_String* y =  T_Nil_String;
    Nodo_String* x = *T;
    //trovo la posizione in cui inserire z nel BST
    while (x != T_Nil_String) {
        y = x;
        if (strcmp(z->id, x->id)<0)
            x = x->left;
        else
            x = x->right;
    }
    z->p = y;
    if (y == T_Nil_String)
        *T = z;
    else if (strcmp(z->id, y->id)<0)
        y->left = z;
    else
        y->right = z;
    //inizializzo z come nodo rosso
    z->left  = T_Nil_String;
    z->right = T_Nil_String;
    z->color = RED;
    RBInsertFixup_String(T, z);

    return &(z->id);
}

//procedura di ricerca di un generico elemento nell'albero delle relazioni
Nodo_Rel* treeSearch_Rel(Nodo_Rel** T, char* id){
    int cmp = 0;
    if((*T) == T_Nil_Rel)
        return (*T);
    else{
        cmp = strcmp(id, (*T)->id);
        if(cmp==0)
            return *T;
        else if(cmp<0)
            return treeSearch_Rel(&((*T)->left), id);
        else
            return treeSearch_Rel(&((*T)->right), id);
    }
}

//procedura di ricerca di un generico elemento nell'albero delle entita' riceventi
Nodo_Dest* treeSearch_Dest(Nodo_Dest** T, char** id){
    int cmp = 0;
    if((*T) == T_Nil_Dest)
        return (*T);
    else{
        cmp = strcmp(*id, *(*T)->id);
        if(cmp==0)
            return *T;
        else if(cmp<0)
            return treeSearch_Dest(&((*T)->left), id);
        else
            return treeSearch_Dest(&((*T)->right), id);
    }
}

//procedura di ricerca di un generico elemento nell'albero delle entita' mittenti
Nodo_Ent* treeSearch_Ent(Nodo_Ent** T, char** id){
    int cmp = 0;
    if((*T) == T_Nil_Ent)
        return (*T);
    else{
        cmp = strcmp(*id, *(*T)->id);
        if(cmp==0)
            return *T;
        else if(cmp<0)
            return treeSearch_Ent(&((*T)->left), id);
        else
            return treeSearch_Ent(&((*T)->right), id);
    }
}

//procedura di ricerca di un generico elemento nell'albero per il report
Nodo_Report* treeSearch_Report(Nodo_Report** T, char** id){
    int cmp = 0;
    if((*T) == T_Nil_Report)
        return (*T);
    else{
        cmp = strcmp(*id, *(*T)->id);
        if(cmp==0)
            return *T;
        else if(cmp<0)
            return treeSearch_Report(&((*T)->left), id);
        else
            return treeSearch_Report(&((*T)->right), id);
    }
}

//procedura di ricerca di un generico elemento nell'albero che monitora tutte le entita'
Nodo_String* treeSearch_String(Nodo_String** T, char* id){
    int cmp = 0;
    if((*T) == T_Nil_String)
        return (*T);
    else{
        cmp = strcmp(id, (*T)->id);
        if(cmp==0)
            return *T;
        else if(cmp<0)
            return treeSearch_String(&((*T)->left), id);
        else
            return treeSearch_String(&((*T)->right), id);
    }
}

//procedura di ricerca del minimo nell'albero delle relazioni
Nodo_Rel* treeMinimum_Rel(Nodo_Rel* x){
    while(x->left!=T_Nil_Rel){
        x = x->left;
    }
    return x;
}

//procedura di ricerca del minimo nell'albero delle entita' riceventi
Nodo_Dest* treeMinimum_Dest(Nodo_Dest* x){
    while(x->left!=T_Nil_Dest){
        x = x->left;
    }
    return x;
}

//procedura di ricerca del minimo nell'albero delle entita' mittenti
Nodo_Ent* treeMinimum_Ent(Nodo_Ent* x){
    while(x->left!=T_Nil_Ent){
        x = x->left;
    }
    return x;
}

//procedura di ricerca del minimo nell'albero per il report
Nodo_Report* treeMinimum_Report(Nodo_Report* x){
    while(x->left!=T_Nil_Report){
        x = x->left;
    }
    return x;
}

//procedura di ricerca del minimo nell'albero che monitora tutte le entita'
Nodo_String* treeMinimum_String(Nodo_String* x){
    while(x->left!=T_Nil_String){
        x = x->left;
    }
    return x;
}

//procedura di ricerca del successore nell'albero delle relazioni
Nodo_Rel* treeSuccessor_Rel(Nodo_Rel* x){
    Nodo_Rel* y;
    if(x->right!=T_Nil_Rel)
        return treeMinimum_Rel(x->right);
    y = x->p;
    while(y!=T_Nil_Rel && x == y->right){ 	//y risale l'albero finche' x non e' il suo figlio sx (x risale a sua volta insieme a y ma di un livello piu' in basso)
        x = y;
        y = y->p;
    }
    return y;
}

//procedura di ricerca del successore nell'albero delle entita' riceventi
Nodo_Dest* treeSuccessor_Dest(Nodo_Dest* x){
    Nodo_Dest* y;
    if(x->right!=T_Nil_Dest)
        return treeMinimum_Dest(x->right);
    y = x->p;
    while(y!=T_Nil_Dest && x == y->right){ 	//y risale l'albero finche' x non e' il suo figlio sx (x risale a sua volta insieme a y ma di un livello piu' in basso)
        x = y;
        y = y->p;
    }
    return y;
}

//procedura di ricerca del successore nell'albero delle entita' mittenti
Nodo_Ent* treeSuccessor_Ent(Nodo_Ent* x){
    Nodo_Ent* y;
    if(x->right!=T_Nil_Ent)
        return treeMinimum_Ent(x->right);
    y = x->p;
    while(y!=T_Nil_Ent && x == y->right){	//y risale l'albero finche' x non e' il suo figlio sx (x risale a sua volta insieme a y ma di un livello piu' in basso)
        x = y;
        y = y->p;
    }
    return y;
}

//procedura di ricerca del successore nell'albero per il report
Nodo_Report* treeSuccessor_Report(Nodo_Report* x){
    Nodo_Report* y;
    if(x->right!=T_Nil_Report)
        return treeMinimum_Report(x->right);
    y = x->p;
    while(y!=T_Nil_Report && x == y->right){	//y risale l'albero finche' x non e' il suo figlio sx (x risale a sua volta insieme a y ma di un livello piu' in basso)
        x = y;
        y = y->p;
    }
    return y;
}

//procedura di ricerca del successore nell'albero che monitora tutte le entita'
Nodo_String* treeSuccessor_String(Nodo_String* x){
    Nodo_String* y;
    if(x->right!=T_Nil_String)
        return treeMinimum_String(x->right);
    y = x->p;
    while(y!=T_Nil_String && x == y->right){ 	//y risale l'albero finche' x non e' il suo figlio sx (x risale a sua volta insieme a y ma di un livello piu' in basso)
        x = y;
        y = y->p;
    }
    return y;
}

//procedura di ripristino delle proprieta' dell'albero RB delle relazioni a seguito di un'eliminazione
void RBDeleteFixup_Rel(Nodo_Rel** T, Nodo_Rel* x){
    Nodo_Rel* w = T_Nil_Rel;
    if(x == T_Nil_Rel)
        return;
    while(x != *T && x->color == BLACK){
        if((x->p->left != T_Nil_Rel) && (x == x->p->left)){
            w = x->p->right;
            if(w == T_Nil_Rel)
                break;
            else{
                if((w->color == RED)){  //caso 1: lo ricollego a uno degli altri casi
                    w->color = BLACK;
                    x->p->color = RED;
                    leftRotate_Rel(T, x->p);
                    w = x->p->right;
                }
                if((w!=T_Nil_Rel) && (w->right!=T_Nil_Rel) && (w->left!=T_Nil_Rel) && (w->left->color == BLACK) && (w->right->color == BLACK)){  //caso 2
                    w->color = RED;
                    x = x->p;
                }
                else{
                    if((w!=T_Nil_Rel) &&(w->right!=T_Nil_Rel) && (w->left!=T_Nil_Rel) && (w->right->color == BLACK)){ //caso 3
                        w->left->color = BLACK;
                        w->color = RED;
                        rightRotate_Rel(T, w);
                        w = x->p->right;
                    }
                    if((w!=T_Nil_Rel)){
                        w->color = x->p->color; //caso 4
                        x->p->color = BLACK;
                        w->right->color = BLACK;
                        leftRotate_Rel(T, x->p);
                        x = *T;
                    }
                }
            }
        }
        else if(x->p!=T_Nil_Rel){
            w = x->p->left;
            if(w == T_Nil_Rel)
                break;
            else{
                if((w->color == RED)){  //caso 1: lo ricollego a uno degli altri casi
                    w->color = BLACK;
                    x->p->color = RED;
                    rightRotate_Rel(T, x->p);
                    w = x->p->left;
                }
                if((w!=T_Nil_Rel) && (w->left!=T_Nil_Rel) && (w->right!=T_Nil_Rel) && (w->right->color == BLACK) && (w->left->color == BLACK)){  //caso 2
                    w->color = RED;
                    x = x->p;
                }
                else{
                    if((w!=T_Nil_Rel) && (w->right!=T_Nil_Rel) && (w->left!=T_Nil_Rel) && (w->left->color == BLACK)){  //caso 3
                        w->right->color = BLACK;
                        w->color = RED;
                        leftRotate_Rel(T, w);
                        w = x->p->left;
                    }
                    if((w!=T_Nil_Rel)){
                        w->color = x->p->color; //caso 4
                        x->p->color = BLACK;
                        if(w->left != T_Nil_Rel)
                            w->left->color = BLACK;
                        rightRotate_Rel(T, x->p);
                        x = *T;
                    }
                }
            }
        }
    }
    x->color = BLACK;
    return;
}

//procedura di ripristino delle proprieta' dell'albero RB delle entita' riceventi a seguito di un'eliminazione
void RBDeleteFixup_Dest(Nodo_Dest** T, Nodo_Dest* x){
    Nodo_Dest* w = T_Nil_Dest;
    if(x == T_Nil_Dest)
        return;
    while(x != *T && x->color == BLACK){
        if((x->p->left != T_Nil_Dest) && (x == x->p->left)){
            w = x->p->right;
            if(w == T_Nil_Dest)
                break;
            else{
                if((w->color == RED)){  //caso 1: lo ricollego a uno degli altri casi
                    w->color = BLACK;
                    x->p->color = RED;
                    leftRotate_Dest(T, x->p);
                    w = x->p->right;
                }
                if((w!=T_Nil_Dest) && (w->right!=T_Nil_Dest) && (w->left!=T_Nil_Dest) && (w->left->color == BLACK) && (w->right->color == BLACK)){  //caso 2
                    w->color = RED;
                    x = x->p;
                }
                else{
                    if((w!=T_Nil_Dest) && (w->right!=T_Nil_Dest) && (w->left!=T_Nil_Dest) && (w->right->color == BLACK)){ //caso 3
                        w->left->color = BLACK;
                        w->color = RED;
                        rightRotate_Dest(T, w);
                        w = x->p->right;
                    }
                    if((w!=T_Nil_Dest)){
                        w->color = x->p->color; //caso 4
                        x->p->color = BLACK;
                        w->right->color = BLACK;
                        leftRotate_Dest(T, x->p);
                        x = *T;
                    }
                }
            }
        }
        else if(x->p!=T_Nil_Dest){
            w = x->p->left;
            if(w == T_Nil_Dest)
                break;
            else{
                if((w->color == RED)){  //caso 1: lo ricollego a uno degli altri casi
                    w->color = BLACK;
                    x->p->color = RED;
                    rightRotate_Dest(T, x->p);
                    w = x->p->left;
                }
                if((w!=T_Nil_Dest) && (w->left!=T_Nil_Dest) && (w->right!=T_Nil_Dest) && (w->right->color == BLACK) && (w->left->color == BLACK)){  //caso 2
                    w->color = RED;
                    x = x->p;
                }
                else{
                    if((w!=T_Nil_Dest) && (w->right!=T_Nil_Dest) && (w->left!=T_Nil_Dest) && (w->left->color == BLACK)){  //caso 3
                        w->right->color = BLACK;
                        w->color = RED;
                        leftRotate_Dest(T, w);
                        w = x->p->left;
                    }
                    if((w!=T_Nil_Dest)){
                        w->color = x->p->color; //caso 4
                        x->p->color = BLACK;
                        if(w->left != T_Nil_Dest)
                            w->left->color = BLACK;
                        rightRotate_Dest(T, x->p);
                        x = *T;
                    }
                }
            }
        }
    }
    x->color = BLACK;
    return;
}

//procedura di ripristino delle proprieta' dell'albero RB delle entita' mittenti a seguito di un'eliminazione
void RBDeleteFixup_Ent(Nodo_Ent** T, Nodo_Ent* x){
    Nodo_Ent* w = T_Nil_Ent;
    if(x == T_Nil_Ent)
        return;
    while(x != *T && x->color == BLACK){
        if((x->p->left != T_Nil_Ent) && (x == x->p->left)){
            w = x->p->right;
            if(w == T_Nil_Ent)
                break;
            else{
                if((w->color == RED)){  //caso 1: lo ricollego a uno degli altri casi
                    w->color = BLACK;
                    x->p->color = RED;
                    leftRotate_Ent(T, x->p);
                    w = x->p->right;
                }
                if((w!=T_Nil_Ent) && (w->left!=T_Nil_Ent) && (w->right!=T_Nil_Ent) && (w->left->color == BLACK) && (w->right->color == BLACK)){  //caso 2
                    w->color = RED;
                    x = x->p;
                }
                else{
                    if((w!=T_Nil_Ent) && (w->right!=T_Nil_Ent) && (w->left!=T_Nil_Ent) && (w->right->color == BLACK)){ //caso 3
                        w->left->color = BLACK;
                        w->color = RED;
                        rightRotate_Ent(T, w);
                        w = x->p->right;
                    }
                    if((w!=T_Nil_Ent)){
                        w->color = x->p->color; //caso 4
                        x->p->color = BLACK;
                        w->right->color = BLACK;
                        leftRotate_Ent(T, x->p);
                        x = *T;
                    }
                }
            }
        }
        else if(x->p!=T_Nil_Ent){
            w = x->p->left;
            if(w == T_Nil_Ent)
                break;
            else{
                if((w->color == RED)){  //caso 1: lo ricollego a uno degli altri casi
                    w->color = BLACK;
                    x->p->color = RED;
                    rightRotate_Ent(T, x->p);
                    w = x->p->left;
                }
                if((w!=T_Nil_Ent) && (w->left!=T_Nil_Ent) && (w->right!=T_Nil_Ent) && (w->right->color == BLACK) && (w->left->color == BLACK)){  //caso 2
                    w->color = RED;
                    x = x->p;
                }
                else{
                    if((w!=T_Nil_Ent) && (w->right!=T_Nil_Ent) && (w->left!=T_Nil_Ent) && (w->left->color == BLACK)){  //caso 3
                        w->right->color = BLACK;
                        w->color = RED;
                        leftRotate_Ent(T, w);
                        w = x->p->left;
                    }
                    if((w!=T_Nil_Ent)){
                        w->color = x->p->color; //caso 4
                        x->p->color = BLACK;
                        if(w->left != T_Nil_Ent)
                            w->left->color = BLACK;
                        rightRotate_Ent(T, x->p);
                        x = *T;
                    }
                }
            }
        }
    }
    x->color = BLACK;
    return;
}

//procedura di ripristino delle proprieta' dell'albero RB per il report a seguito di un'eliminazione
void RBDeleteFixup_Report(Nodo_Report** T, Nodo_Report* x){
    Nodo_Report* w = T_Nil_Report;
    if(x == T_Nil_Report)
        return;
    while(x != *T && x->color == BLACK){
        if((x->p->left != T_Nil_Report) && (x == x->p->left)){
            w = x->p->right;
            if(w == T_Nil_Report)
                break;
            else{
                if((w->color == RED)){  //caso 1: lo ricollego a uno degli altri casi
                    w->color = BLACK;
                    x->p->color = RED;
                    leftRotate_Report(T, x->p);
                    w = x->p->right;
                }
                if((w!=T_Nil_Report) && (w->right!=T_Nil_Report) && (w->left!=T_Nil_Report) && (w->left->color == BLACK) && (w->right->color == BLACK)){  //caso 2
                    w->color = RED;
                    x = x->p;
                }
                else{
                    if((w!=T_Nil_Report) && (w->right!=T_Nil_Report) && (w->left!=T_Nil_Report) && (w->right->color == BLACK)){ //caso 3
                        w->left->color = BLACK;
                        w->color = RED;
                        rightRotate_Report(T, w);
                        w = x->p->right;
                    }
                    if((w!=T_Nil_Report)){
                        w->color = x->p->color; //caso 4
                        x->p->color = BLACK;
                        w->right->color = BLACK;
                        leftRotate_Report(T, x->p);
                        x = *T;
                    }
                }
            }
        }
        else if(x->p!=T_Nil_Report){
            w = x->p->left;
            if(w == T_Nil_Report)
                break;
            else{
                if((w->color == RED)){  //caso 1: lo ricollego a uno degli altri casi
                    w->color = BLACK;
                    x->p->color = RED;
                    rightRotate_Report(T, x->p);
                    w = x->p->left;
                }
                if((w!=T_Nil_Report) && (w->left!=T_Nil_Report) && (w->right!=T_Nil_Report) && (w->right->color == BLACK) && (w->left->color == BLACK)){  //caso 2
                    w->color = RED;
                    x = x->p;
                }
                else{
                    if((w!=T_Nil_Report) && (w->right!=T_Nil_Report) && (w->left!=T_Nil_Report) && (w->left->color == BLACK)){  //caso 3
                        w->right->color = BLACK;
                        w->color = RED;
                        leftRotate_Report(T, w);
                        w = x->p->left;
                    }
                    if((w!=T_Nil_Report)){
                        w->color = x->p->color; //caso 4
                        x->p->color = BLACK;
                        if(w->left != T_Nil_Report)
                            w->left->color = BLACK;
                        rightRotate_Report(T, x->p);
                        x = *T;
                    }
                }
            }
        }
    }
    x->color = BLACK;
    return;
}

//procedura di ripristino delle proprieta' dell'albero RB che monitora tutte le entita' a seguito di un'eliminazione
void RBDeleteFixup_String(Nodo_String** T, Nodo_String* x){
    Nodo_String* w = T_Nil_String;
    if(x == T_Nil_String)
        return;
    while(x != *T && x->color == BLACK){
        if((x->p->left != T_Nil_String) && (x == x->p->left)){
            w = x->p->right;
            if(w == T_Nil_String)
                break;
            else{
                if((w->color == RED)){  //caso 1: lo ricollego a uno degli altri casi
                    w->color = BLACK;
                    x->p->color = RED;
                    leftRotate_String(T, x->p);
                    w = x->p->right;
                }
                if((w!=T_Nil_String) && (w->right!=T_Nil_String) && (w->left!=T_Nil_String) && (w->left->color == BLACK) && (w->right->color == BLACK)){  //caso 2
                    w->color = RED;
                    x = x->p;
                }
                else{
                    if((w!=T_Nil_String) && (w->right!=T_Nil_String) && (w->left!=T_Nil_String) && (w->right->color == BLACK)){ //caso 3
                        w->left->color = BLACK;
                        w->color = RED;
                        rightRotate_String(T, w);
                        w = x->p->right;
                    }
                    if((w!=T_Nil_String)){
                        w->color = x->p->color; //caso 4
                        x->p->color = BLACK;
                        w->right->color = BLACK;
                        leftRotate_String(T, x->p);
                        x = *T;
                    }
                }
            }
        }
        else if(x->p!=T_Nil_String){
            w = x->p->left;
            if(w == T_Nil_String)
                break;
            else{
                if((w->color == RED)){  //caso 1: lo ricollego a uno degli altri casi
                    w->color = BLACK;
                    x->p->color = RED;
                    rightRotate_String(T, x->p);
                    w = x->p->left;
                }
                if((w!=T_Nil_String) && (w->left!=T_Nil_String) && (w->right!=T_Nil_String) && (w->right->color == BLACK) && (w->left->color == BLACK)){  //caso 2
                    w->color = RED;
                    x = x->p;
                }
                else{
                    if((w!=T_Nil_String) && (w->right!=T_Nil_String) && (w->left!=T_Nil_String) && (w->left->color == BLACK)){  //caso 3
                        w->right->color = BLACK;
                        w->color = RED;
                        leftRotate_String(T, w);
                        w = x->p->left;
                    }
                    if((w!=T_Nil_String)){
                        w->color = x->p->color; //caso 4
                        x->p->color = BLACK;
                        if(w->left != T_Nil_String)
                            w->left->color = BLACK;
                        rightRotate_String(T, x->p);
                        x = *T;
                    }
                }
            }
        }
    }
    x->color = BLACK;
    return;
}

//procedura di rimozione di un nodo dall'albero delle relazioni
void RBDelete_Rel(Nodo_Rel** T, Nodo_Rel* z){
    Nodo_Rel* y = T_Nil_Rel;
    Nodo_Rel* x = T_Nil_Rel;
    if(z->left == T_Nil_Rel || z->right == T_Nil_Rel)
        y = z;
    else
        y = treeSuccessor_Rel(z);
    if(y->left != T_Nil_Rel)
        x = y->left;
    else
        x = y->right;
    x->p = y->p;
    if(y->p == T_Nil_Rel)
        *T = x;
    else if(y == y->p->left)
        y->p->left = x;
    else
        y->p->right = x;
    if(y != z){    //copio i dati di y (che e' il successore di z)
        free(z->id);
        z->id = (char*)malloc(sizeof(char)*(strlen(y->id)+1));
        strcpy(z->id, y->id);
        z->deep = y->deep;
        z->max = y->max;
        z->report = y->report;
    }
    if(y->color == BLACK)
        RBDeleteFixup_Rel(T, x);    //x e' l'unico figlio di y prima che fosse estratto oppure T_Nil se y non aveva figli
    free(y->id);
    y->id = NULL;
    free(y);
    y = NULL;
    return;
}

//procedura di rimozione di un nodo dall'albero delle entita' riceventi
void RBDelete_Dest(Nodo_Dest** T, Nodo_Dest* z){
    Nodo_Dest* y = T_Nil_Dest;
    Nodo_Dest* x = T_Nil_Dest;
    if(z->left == T_Nil_Dest || z->right == T_Nil_Dest)
        y = z;
    else
        y = treeSuccessor_Dest(z);
    if(y->left != T_Nil_Dest)
        x = y->left;
    else
        x = y->right;
    x->p = y->p;
    if(y->p == T_Nil_Dest)
        *T = x;
    else if(y == y->p->left)
        y->p->left = x;
    else
        y->p->right = x;
    if(y != z){    //copio i dati di y (che e' il successore di z)
        z->id = y->id;
        z->deep = y->deep;
        z->num_of_rel = y->num_of_rel;
    }
    if(y->color == BLACK)
        RBDeleteFixup_Dest(T, x);    //x e' l'unico figlio di y prima che fosse estratto oppure T_Nil se y non aveva figli
    free(y);
    y = NULL;
    return;
}

//procedura di rimozione di un nodo dall'albero delle entita' mittenti
void RBDelete_Ent(Nodo_Ent** T, Nodo_Ent* z){
    Nodo_Ent* y = T_Nil_Ent;
    Nodo_Ent* x = T_Nil_Ent;

    if(z->left == T_Nil_Ent || z->right == T_Nil_Ent)
        y = z;
    else
        y = treeSuccessor_Ent(z);
    if(y->left != T_Nil_Ent)
        x = y->left;
    else
        x = y->right;
    x->p = y->p;
    if(y->p == T_Nil_Ent)
        *T = x;
    else if(y == y->p->left)
        y->p->left = x;
    else
        y->p->right = x;
    if(y != z){    //copio i dati di y (che e' il successore di z)
        z->id = y->id;
    }
    if(y->color == BLACK)
        RBDeleteFixup_Ent(T, x);    //x e' l'unico figlio di y prima che fosse estratto oppure T_Nil se y non aveva figli
    free(y);
    y = NULL;
    return;
}

//procedura di rimozione di un nodo dall'albero per il report
void RBDelete_Report(Nodo_Report** T, Nodo_Report* z){
    Nodo_Report* y = T_Nil_Report;
    Nodo_Report* x = T_Nil_Report;

    if(z == T_Nil_Report)
        return;
    if(z->left == T_Nil_Report || z->right == T_Nil_Report)
        y = z;
    else
        y = treeSuccessor_Report(z);
    if(y->left != T_Nil_Report)
        x = y->left;
    else
        x = y->right;
    x->p = y->p;
    if(y->p == T_Nil_Report)
        *T = x;
    else if(y == y->p->left)
        y->p->left = x;
    else
        y->p->right = x;
    if(y != z){    //copio i dati di y (che e' il successore di z)
        z->id = y->id;
    }
    if(y->color == BLACK)
        RBDeleteFixup_Report(T, x);    //x e' l'unico figlio di y prima che fosse estratto oppure T_Nil se y non aveva figli
    free(y);
    y = NULL;
    return;
}

//procedura che fa puntare alla stringa corretta tutti i puntatori a stringa (degli RBT di riceventi e mittenti) interessati dalla rimozione di un nodo dall'RBT che monitora tutte le entita'
//(dato che nella RB_Delete viene copiato il nodo successore al posto del nodo da eliminare, bisogna far si' che i puntatori a stringa puntino al nuovo indirizzo che contiene
// l'identificativo dell'entita' appena copiata)
//SENZA QUESTA FUNZIONE SI AVREBBERO OUTPUT SCORRETTI
void recursiveFix(Nodo_Dest** T_Dest, char** id){
    Nodo_Ent* ent = T_Nil_Ent;

    if((*T_Dest)->left != T_Nil_Dest)
        recursiveFix(&((*T_Dest)->left), id);
    if((*T_Dest)->right != T_Nil_Dest)
        recursiveFix(&((*T_Dest)->right), id);
    if(strcmp(*(*T_Dest)->id, *id)==0)
        (*T_Dest)->id = id;
    if((*T_Dest)->deep != T_Nil_Ent){
        ent = treeSearch_Ent(&((*T_Dest)->deep), id);
        if(ent != T_Nil_Ent)
            ent->id = id;
    }

    return;
}

//procedura che fa puntare alla stringa corretta tutti i puntatori a stringa (dell'RBT per il report) interessati dalla rimozione di un nodo dall'RBT che monitora tutte le entita'
//(dato che nella RB_Delete viene copiato il nodo successore al posto del nodo da eliminare, bisogna far si' che i puntatori a stringa puntino al nuovo indirizzo che contiene
// l'identificativo dell'entita' appena copiata)
//SENZA QUESTA FUNZIONE SI AVREBBERO OUTPUT SCORRETTI
void recursiveRepFix(Nodo_Report** T, char** id){
    if(((*T)->left) != T_Nil_Report)
        recursiveRepFix(&((*T)->left), id);
    if(((*T)->right) != T_Nil_Report)
        recursiveRepFix(&((*T)->right), id);
    if(strcmp(*(*T)->id, *id)==0)
        (*T)->id = id;
}

//procedura che chiama le procedure ricorsive che sistemano la questione dei puntatori a stringa a seguito della rimozione di un nodo dall'RBT che monitora tutte le entita'
//SENZA QUESTA FUNZIONE SI AVREBBERO OUTPUT SCORRETTI
void fixSuccessorDelete(Nodo_Rel** T_Rel, char** id){
    //chiamo ricorsivamente la funzione su tutte le relazioni
    if(*T_Rel != T_Nil_Rel){
        fixSuccessorDelete(&((*T_Rel)->left), id);
        fixSuccessorDelete(&((*T_Rel)->right), id);
        if((*T_Rel)->report != T_Nil_Report)
            recursiveRepFix(&((*T_Rel)->report), id);
        if((*T_Rel)->deep != T_Nil_Dest)
            recursiveFix(&((*T_Rel)->deep), id);
    }

    return;
}

//procedura di rimozione di un nodo dall'albero che monitora tutte le entita'
void RBDelete_String(Nodo_Rel** T_Rel, Nodo_String** T_String, Nodo_String* z){
    Nodo_String* y = T_Nil_String;
    Nodo_String* x = T_Nil_String;

    if(z->left == T_Nil_String || z->right == T_Nil_String)
        y = z;
    else
        y = treeSuccessor_String(z);
    if(y->left != T_Nil_String)
        x = y->left;
    else
        x = y->right;
    x->p = y->p;
    if(y->p == T_Nil_String)
        *T_String = x;
    else if(y == y->p->left)
        y->p->left = x;
    else
        y->p->right = x;
    if(y != z){    //copio i dati di y (che e' il successore di z)
        free(z->id);
        z->id = (char*)malloc(sizeof(char)*(strlen(y->id)+1));
        strcpy(z->id, y->id);
        fixSuccessorDelete(T_Rel, &(z->id));
    }
    if(y->color == BLACK)
        RBDeleteFixup_String(T_String, x);    //x e' l'unico figlio di y prima che fosse estratto oppure T_Nil se y non aveva figli
    free(y->id);
    y->id = NULL;
    free(y);
    y = NULL;
    return;
}

//procedura di ricerca del maggior numero di relazioni entranti per una certa relazione
Nodo_Dest* verificaMax(Nodo_Dest** T, Nodo_Dest* dest){
    Nodo_Dest* maxNode = dest;
    Nodo_Dest* maxNode_left = T_Nil_Dest;
    Nodo_Dest* maxNode_right = T_Nil_Dest;

    if(*T != T_Nil_Dest){
        if((*T)->num_of_rel > dest->num_of_rel)    //se trovo un'entita' il cui numero di relazioni e' maggiore del numero di relazioni dell'entita' esaminata, allora essa diventa la nuova entita' massima
            maxNode = *T;
        maxNode_left = verificaMax(&((*T)->left), maxNode);    //maxNode_left e' l'entita' col magglor numero di relazioni entranti appartenente al sottoalbero sx dell'RBT delle entita'� riceventi
        maxNode_right = verificaMax(&((*T)->right), maxNode);    //maxNode_right e' l'entita' col maggior numero di relazioni entranti appartenente al sottoalbero dx dell'RBT delle entita' riceventi
    }
    else
        return T_Nil_Dest;
    //quindi decido quale nodo restituire
    if(maxNode == dest && maxNode == maxNode_left && maxNode == maxNode_right)
        return dest;
    else{
        if((maxNode->num_of_rel >= maxNode_left->num_of_rel) && (maxNode->num_of_rel >= maxNode_right->num_of_rel))
            return maxNode;     //maxNode e' l'entita' di partenza se e solo se la radice dell'RBT delle entita' riceventi ha un numero di relazioni minore o uguale al suo
                                //altrimenti maxNode e' la radice dell'RBT delle entita' riceventi
        else if ((maxNode->num_of_rel < maxNode_left->num_of_rel) && (maxNode_left->num_of_rel >= maxNode_right->num_of_rel))
            return maxNode_left;
        else
            return maxNode_right;
    }
}

//procedura di aggiunta di un'entita' nell'RBT di tutte le entita'
void addEnt(Nodo_String** T, char* id_ent){
    Nodo_String* x = treeSearch_String(T, id_ent);
    if(x == T_Nil_String)  //se il nodo non e' presente nell'RBT di tutte le entita' lo inserisco, altrimenti non faccio nulla
        RBInsert_String(T, id_ent);
    return;
}

//procedura che svuota l'albero per il report
void emptyReport(Nodo_Report** T){
    if(*T != T_Nil_Report){
        emptyReport(&((*T)->left));
        emptyReport(&((*T)->right));
        RBDelete_Report(T, *T);
    }
    return;
}

//procedura di aggiunta di una relazione fra due entita'
void addRel(Nodo_Rel** T_Rel, char** id_orig, char** id_dest, char* id_rel){
    Nodo_Rel* rel = treeSearch_Rel(T_Rel, id_rel);
    Nodo_Dest* dest = T_Nil_Dest;
    Nodo_Ent* ent = T_Nil_Ent;

    if(rel == T_Nil_Rel)
        rel = RBInsert_Rel(T_Rel, id_rel);
    dest = treeSearch_Dest(&(rel->deep), id_dest);
    if(dest == T_Nil_Dest)
        dest = RBInsert_Dest(&(rel->deep), id_dest);
    ent = treeSearch_Ent(&(dest->deep), id_orig);
    if(ent == T_Nil_Ent){
        ent = RBInsert_Ent(&(dest->deep), id_orig);
        (dest->num_of_rel) = (dest->num_of_rel) + 1;
        if(rel->max == T_Nil_Dest || dest->num_of_rel > rel->max->num_of_rel){	//se il nodo che riceve la relazione ha un numero di relazioni entranti maggiore dell'attuale massimo, allora
        																		//diventa esso stesso il nuovo massimo
            rel->max = dest;
    	}
        if(rel->max == dest){   //se il massimo e' cambiato, svuoto e inserisco
            emptyReport(&(rel->report));
            RBInsert_Report(&(rel->report), rel->max->id);
        }
        else{	//altrimenti controllo se il nodo che riceve la relazione ha un numero di relazioni entranti uguale a quello dell'attuale massimo 
            if(dest->num_of_rel == rel->max->num_of_rel && treeSearch_Report(&(rel->report), dest->id) == T_Nil_Report)	
                RBInsert_Report(&(rel->report), dest->id);	//quindi lo inserisco se non era gia' presente
        }
    }

    return;
}

//procedura che inserisce tutti i nodi con uguale numero (massimo) di relazioni entranti nell'RBT del report
void fillReport(Nodo_Dest** T_Dest, Nodo_Report** T_Report, int max){
    if(*T_Dest != T_Nil_Dest){
        fillReport(&((*T_Dest)->left), T_Report, max);
        fillReport(&((*T_Dest)->right), T_Report, max);
        if((*T_Dest)->num_of_rel == max && treeSearch_Report(T_Report, (*T_Dest)->id) == T_Nil_Report)
            RBInsert_Report(T_Report, (*T_Dest)->id);
    }
    return;
}

//procedura di rimozione di un'istanza di una certa relazione fra due entita'
void delRel(Nodo_Rel** T_Rel, char** id_orig, char** id_dest, char* id_rel){
    Nodo_Rel* rel = treeSearch_Rel(T_Rel, id_rel);
    Nodo_Dest* dest = T_Nil_Dest;
    Nodo_Ent* ent = T_Nil_Ent;
    int flag = 0;
    if(rel != T_Nil_Rel){
        dest = treeSearch_Dest(&(rel->deep), id_dest);
        if(dest != T_Nil_Dest){
            ent = treeSearch_Ent(&(dest->deep), id_orig);
            if(ent != T_Nil_Ent){
                RBDelete_Ent(&(dest->deep), ent);
                (dest->num_of_rel) = (dest->num_of_rel) - 1;
                if(dest->num_of_rel == 0){
                    RBDelete_Report(&(rel->report), treeSearch_Report(&(rel->report), dest->id));
                    RBDelete_Dest(&(rel->deep), dest);
                    flag = 1;
                }
                if(dest == rel->max)
                    rel->max = verificaMax(&(rel->deep), rel->deep); //devo verificare se a seguito della rimozione dell'istanza della relazione non sia cambiata anche l'entia'
																	 //col maggior numero di relazioni entranti
                if(flag != 1){
                    if(rel->max != T_Nil_Dest && dest->num_of_rel < rel->max->num_of_rel)    //se dopo la delrel il nodo in questione non e' piu' il massimo nonostante prima lo fosse
                        RBDelete_Report(&(rel->report), treeSearch_Report(&(rel->report), dest->id));
                    else if(rel->max != T_Nil_Dest && dest->num_of_rel == rel->max->num_of_rel){   //se dest e' il massimo o e' tornato ad essere uno dei massimi, svuoto e reinserisco
                        emptyReport(&(rel->report));
                        fillReport(&(rel->deep), &(rel->report), rel->max->num_of_rel);
                    }
                }
            }
        }
    }
    if(rel->deep == T_Nil_Dest){
        emptyReport(&(rel->report));
        RBDelete_Rel(T_Rel, rel);   //se l'RBT delle entita' riceventi e' vuoto, ovvero se non ci sono piu' istanze, viene eliminata
    }
    return;
}

//procedura che elimina, per ogni entita' ricevente, tutte le istanze in cui e' presente id_ent
void recursiveDelete(char** id_ent, Nodo_Dest** T_Dest){
    Nodo_Ent* ent = T_Nil_Ent;

    if((*T_Dest)->left != T_Nil_Dest)
        recursiveDelete(id_ent, &((*T_Dest)->left));
    if((*T_Dest)->right != T_Nil_Dest)
        recursiveDelete(id_ent, &((*T_Dest)->right));
    ent = treeSearch_Ent(&((*T_Dest)->deep), id_ent);
    if(ent != T_Nil_Ent){
        RBDelete_Ent(&((*T_Dest)->deep), ent);
        ((*T_Dest)->num_of_rel) = ((*T_Dest)->num_of_rel) - 1;
        if((*T_Dest)->num_of_rel == 0)
            RBDelete_Dest(T_Dest, *T_Dest);
    }

    return;
}

//procedura che elimina tutte le entita' mittenti per una certa entita' ricevente
void deleteAllEnt(Nodo_Ent** T){
    if(*T != T_Nil_Ent){
        deleteAllEnt(&((*T)->left));
        deleteAllEnt(&((*T)->right));
        RBDelete_Ent(T, *T);
    }
    return;
}

//procedura che rimuove un'entita' completamente dal programma
void delEnt(Nodo_Rel** T_Rel, char** id_ent){
    Nodo_Dest* dest = T_Nil_Dest;

    //chiamo ricorsivamente la funzione su tutte le relazioni
    if((*T_Rel)->left != T_Nil_Rel)
        delEnt(&((*T_Rel)->left), id_ent);
    if((*T_Rel)->right != T_Nil_Rel)
        delEnt(&((*T_Rel)->right), id_ent);
    dest = treeSearch_Dest(&((*T_Rel)->deep), id_ent);   //cerco l'entita' da eliminare nel sottoalbero di ogni relazione per eliminarla dalle entita' riceventi
    if(dest != T_Nil_Dest){
        RBDelete_Report(&((*T_Rel)->report), treeSearch_Report(&((*T_Rel)->report), dest->id));
        deleteAllEnt(&(dest->deep));
        RBDelete_Dest(&((*T_Rel)->deep), dest);
    }
    if((*T_Rel)->deep != T_Nil_Dest)    //verifico se a seguito delle eliminazioni non ho piu' nessuna istanza
        recursiveDelete(id_ent, &((*T_Rel)->deep));
    //controllo chi e' il nuovo max dopo le rimozioni di ogni istanza
    if((*T_Rel)->deep == T_Nil_Dest){
        emptyReport(&((*T_Rel)->report));
        RBDelete_Rel(T_Rel, *T_Rel);
    }
    else{
        (*T_Rel)->max = verificaMax(&((*T_Rel)->deep), (*T_Rel)->deep);
        emptyReport(&((*T_Rel)->report));
        fillReport(&((*T_Rel)->deep), &((*T_Rel)->report), (*T_Rel)->max->num_of_rel);
    }

    return;
}

//procedura di ricerca e stampa di tutte le entita' col maggior numero di relazioni entranti
void findAllMax_and_print(Nodo_Report** T){
    if((*T) != T_Nil_Report){
        findAllMax_and_print(&((*T)->left));
        fputs("\"", stdout);
        fputs(*((*T)->id), stdout);
        fputs("\" ", stdout);
        findAllMax_and_print(&((*T)->right));
    }

    return;
}

//procedura che stampa per ogni relazione le entita' col maggior numero di relazioni entranti
void report(Nodo_Rel** T){
    //il report si basa su una doppia visita in ordine simmetrico (una per le entita' riceventi) e una per le relazioni, in modo da
    //avere sia le relazioni in ordine sia le varie entita' per ogni relazione
    if(*T != T_Nil_Rel){
        report(&((*T)->left));
        if(flag == TRUE){
            fputs("\"", stdout);
            flag = FALSE;
        }
        else
            fputs(" \"", stdout);
        fputs(((*T)->id), stdout);  //stampo il nome della relazione
        fputs("\" ", stdout);
        findAllMax_and_print(&((*T)->report));  //stampo tutte le entita' col maggior numero di relazioni entranti per la relazione in questione
        printf("%d;", ((*T)->max)->num_of_rel);   //stampo il maggior numero di relazioni entranti
        report(&((*T)->right));
    }

    return;
}

int main(void){
    Nodo_Rel* Root_Rel = T_Nil_Rel;
    Nodo_String* Root_String = T_Nil_String;
    Nodo_String* ent = T_Nil_String;  //utile per la delEnt
    Nodo_String* orig = T_Nil_String;
    Nodo_String* dest = T_Nil_String;
    char* input = (char*)malloc(sizeof(char)*130);
    char* command = (char*)malloc(sizeof(char)*7);
    char* id_1 = (char*)malloc(sizeof(char)*40);
    char* id_2 = (char*)malloc(sizeof(char)*40);
    char* id_rel = (char*)malloc(sizeof(char)*40);
    char* temp = NULL;
    char* temp2 = NULL;

    while(1){
        fgets(input, 130, stdin);
        temp = input;
        temp2 = command;
        while(1){
            if(*input == ' ' || *input == '\n'){
                *command = '\0';
                break;
            }
            *command = *input;
            command++;
            input++;
        }
        command = temp2;
        if(strcmp(command, "end") == 0)
            break;
        else if(strcmp(command, "report") == 0){
            if(Root_Rel == T_Nil_Rel)
                printf("none");
            else
                report(&Root_Rel);
            printf("\n");
            flag = TRUE;
        }
        else if(strcmp(command, "addent") == 0){
            input += 2; //salto le virgolette
            temp2 = id_1;
            while(1){
                if(*input == '\"'){
                    *id_1 = '\0';
                    break;
                }
                *id_1 = *input;
                id_1++;
                input++;
            }
            id_1 = temp2;
            addEnt(&Root_String, id_1);
        }
        else if(strcmp(command, "delent") == 0){
            input += 2; //salto le virgolette
            temp2 = id_1;
            while(1){
                if(*input == '\"'){
                    *id_1 = '\0';
                    break;
                }
                *id_1 = *input;
                id_1++;
                input++;
            }
            id_1 = temp2;
            ent = treeSearch_String(&Root_String, id_1);
            if(ent != T_Nil_String){    //se l'entita' e' monitorata
                if(Root_Rel!=T_Nil_Rel)
                    delEnt(&Root_Rel, &id_1);
                RBDelete_String(&Root_Rel, &Root_String, ent);
            }
        }
        else if(strcmp(command, "addrel") == 0){
            input += 2; //salto le virgolette
            temp2 = id_1;
            while(1){
                if(*input == '\"'){
                    *id_1 = '\0';
                    break;
                }
                *id_1 = *input;
                id_1++;
                input++;
            }
            id_1 = temp2;
            input += 3; //salto lo spazio e le virgolette
            temp2 = id_2;
            while(1){
                if(*input == '\"'){
                    *id_2 = '\0';
                    break;
                }
                *id_2 = *input;
                id_2++;
                input++;
            }
            id_2 = temp2;
            input += 3; //salto lo spazio e le virgolette
            temp2 = id_rel;
            while(1){
                if(*input == '\"'){
                    *id_rel = '\0';
                    break;
                }
                *id_rel = *input;
                id_rel++;
                input++;
            }
            id_rel = temp2;
            orig = treeSearch_String(&Root_String, id_1);
            dest = treeSearch_String(&Root_String, id_2);
            if(orig != T_Nil_String && dest != T_Nil_String)    //se entrambe le entita' sono monitorate
                addRel(&Root_Rel, &(orig->id), &(dest->id), id_rel);
        }
        else if(strcmp(command, "delrel") == 0){
            input += 2; //salto le virgolette
            temp2 = id_1;
            while(1){
                if(*input == '\"'){
                    *id_1 = '\0';
                    break;
                }
                *id_1 = *input;
                id_1++;
                input++;
            }
            id_1 = temp2;
            input += 3; //salto lo spazio e le virgolette
            temp2 = id_2;
            while(1){
                if(*input == '\"'){
                    *id_2 = '\0';
                    break;
                }
                *id_2 = *input;
                id_2++;
                input++;
            }
            id_2 = temp2;
            input += 3; //salto lo spazio e le virgolette
            temp2 = id_rel;
            while(1){
                if(*input == '\"'){
                    *id_rel = '\0';
                    break;
                }
                *id_rel = *input;
                id_rel++;
                input++;
            }
            id_rel = temp2;
            orig = treeSearch_String(&Root_String, id_1);
            dest = treeSearch_String(&Root_String, id_2);
            if(orig != T_Nil_String && dest != T_Nil_String)   //se entrambe le entita' sono monitorate
                delRel(&Root_Rel, &(orig->id), &(dest->id), id_rel);
        }
        input = temp;
    }

    return 0;
}
