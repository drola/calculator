%language "Java"
%name-prefix "Calc"
%define parser_class_name "Calc"
%define public

%token <Double> REAL
%token <Value> UNIT
%token <dfunc> RFUNC
%token <ufunc> UFUNC
%token <integer> EXPONENT
%token <integer> MULTIPLY
%token <integer> MULTSTAR
%token <integer> DIVIDE
%token <integer> NUMDIV
%token <integer> SQRT
%token <integer> CUBEROOT
%token <integer> MULTMINUS
%token <integer> EOL
%token <integer> FUNCINV
%token <integer> SCANERROR
%token <integer> BADNUMBER


%type <number> numexpr
%type <utype> expr
%type <utype> list
%type <utype> pexpr
%type <utype> unitexpr


%left ADD MINUS
%left UNARY
%left DIVIDE MULTSTAR
%left MULTIPLY MULTMINUS
%nonassoc '(' SQRT CUBEROOT RFUNC UNIT REAL UFUNC FUNCINV SCANERROR BADNUMBER
%right EXPONENT
%left NUMDIV


%%
 input: EOL          { COMM->result = makenumunit(1,&err); CHECK; YYACCEPT; }
      | unitexpr EOL { COMM->result = $1; YYACCEPT; }
      | error        { YYABORT; }
      ;

 unitexpr:  expr                    {$$ = $1;}
         |  DIVIDE list             { invertunit($2); $$=$2;}
         ;

 expr: list                         { $$ = $1; }
     | MULTMINUS list %prec UNARY   { $$ = $2; $$->factor *= -1; }
     | MINUS list %prec UNARY       { $$ = $2; $$->factor *= -1; }
     | expr ADD expr                { err = addunit($1,$3); CHECK; $$=$1;}
     | expr MINUS expr              { $3->factor *= -1; err = addunit($1,$3); 
                                         CHECK; $$=$1;}
     | expr DIVIDE expr             { err = divunit($1, $3); CHECK; $$=$1;}
     | expr MULTIPLY expr           { err = multunit($1,$3); CHECK; $$=$1;}
     | expr MULTSTAR expr           { err = multunit($1,$3); CHECK; $$=$1;}
     ; 

 numexpr:  REAL                     { $$ = $1;         }
         | numexpr NUMDIV numexpr   { $$ = $1 / $3;    }
     ;

 pexpr: '(' expr ')'                { $$ = $2;  }
       ;

 /* list is a list of units, possibly raised to powers, to be multiplied
    together. */

 list:  numexpr                    { $$ = makenumunit($1,&err); CHECK;}
      | UNIT                       { $$ = $1; }
      | list EXPONENT list         { err = unitpower($1,$3); CHECK; $$=$1;}
      | list MULTMINUS list        { err = multunit($1,$3); CHECK; $$=$1;}
      | list list %prec MULTIPLY   { err = multunit($1,$2); CHECK; $$=$1;}
      | pexpr                      { $$=$1; }
      | SQRT pexpr                 { err = rootunit($2,2); CHECK; $$=$2;}
      | CUBEROOT pexpr             { err = rootunit($2,3); CHECK; $$=$2;}
      | RFUNC pexpr                { err = funcunit($2,$1); CHECK; $$=$2;}
      | UFUNC pexpr                { err = evalfunc($2,$1,0); CHECK; $$=$2;}
      | FUNCINV UFUNC pexpr        { err = evalfunc($3,$2,1); CHECK; $$=$3;}
      | list EXPONENT MULTMINUS list %prec EXPONENT  
                                   { $4->factor *= -1;
				   err = unitpower($1,$4); CHECK; $$=$1;}
      | list EXPONENT MINUS list %prec EXPONENT  
                                   { $4->factor *= -1;
				   err = unitpower($1,$4); CHECK; $$=$1;}
      | BADNUMBER                  { err = E_BADNUM; CHECK; }
      | SCANERROR                  { err = E_PARSEMEM; CHECK; }        
   ;



