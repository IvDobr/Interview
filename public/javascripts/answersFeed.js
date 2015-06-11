function Answer (answerId, userName, answerDate, answerInterview, chekedVariants) {
    var self = this;
    self.answerId = answerId;
    self.userName = ko.observable(userName);
    self.answerDate = ko.observable(answerDate);
    self.answerInterview = ko.observable(answerInterview);
    self.chekedVariants = ko.observableArray(chekedVariants);
}

function Variant (question, checkedVariant) {
    var self = this;
    self.question = ko.observable(question);
    self.checkedVariant = ko.observable(checkedVariant);
}


InterviewList = function() {
    var self = this;

    self.pageSize           = ko.observable(20);
    self.currentPage        = ko.observable(1);
    self.countAnswers       = ko.observable("");
    self.totalPages         = ko.observable("");
    self.pages              = ko.observableArray([]);
    self.search             = ko.observable("");
    self.sorter             = ko.observable("");

    self.answersList        = ko.observableArray([]);

    self.userName           = ko.observable("");
    self.answerDate         = ko.observable("");
    self.answerInterview    = ko.observable("");

    self.variantsList       = ko.observableArray([]);

    self.ans;
    self.userName_modal             = ko.observable("");
    self.answerDate_modal           = ko.observable("");
    self.answerInterview_modal      = ko.observable("");
    self.question                   = ko.observable("");
    self.checkedVariant             = ko.observable("");

    self.getDiagrams = function(){
        var id = $("param[name='id']").attr("value");
        if (0 != id) {
            jsRoutes.controllers.API.getGraphsJSON(id).ajax({
                success: function (data) {
                    var graphs = data.graphs;
                    for (var i = 0; i < graphs.length; i++) {
                        $( "#graphs" ).append( "<div id=\"" + graphs[i].questionId + "\"></div>");
                            var revenueChart = new FusionCharts({
                                "type": graphs[i].type_of_this_diagram,
                                "renderAt": graphs[i].questionId.toString(),
                                "width": "800",
                                "height": "300",
                                "dataFormat": "json",
                                "dataSource": graphs[i]
                            });
                            revenueChart.render();
                    }
                },
                error: function () {
                    console.log('Не могу отправить json запрос');
                }
            });
        }
    };

    self.getList = function(){
        var id = $("param[name='id']").attr("value");

        jsRoutes.controllers.API.getAnswersJSON().ajax({
            dataType : 'json',
            contentType : 'charset=utf-8',
            data : {
                intId: id,
                pageSize: parseInt(self.pageSize()),
                currentPage: parseInt(self.currentPage()),
                sortby: self.sorter,
                search: self.search
            },
            success : function(data) {
                self.countAnswers(data.countAnswers);
                self.totalPages(data.pages);

                var c = parseInt(self.currentPage());

                self.pages.removeAll();
                for (var j = 1; j <= data.pages; j++) self.pages.push(j);

                self.currentPage(c);

                var o = data.answersList;
                self.answersList.removeAll();
                for (var i = 0; i < o.length; i++) {

                    var p = o[i].check_variants;
                    var vars = [];
                    for(j = 0; j < p.length; j++) {
                        vars.push(
                            new Variant(
                                p[j].question,
                                p[j].answer
                            )
                        )
                    }
                    self.answersList.push(
                        new Answer(
                            o[i].answerId,
                            o[i].userName,
                            o[i].createDate,
                            o[i].interview,
                            vars
                        )
                    );
                }
            },
            error : function() {
                console.log('Не могу отправить json запрос');
            }
        });
    };

    self.seeVariants = function(answer){
        self.userName_modal(answer.userName());
        self.answerDate_modal(answer.answerDate());
        self.answerInterview_modal(answer.answerInterview());
        self.variantsList(answer.chekedVariants());
        self.ans = answer;
        $('#seeVariants').modal('show');
    };

    self.removeAnswer = function(answer){
        if (null == answer.answerId) answer = self.ans;
        jsRoutes.controllers.API.removeAnswerJSON().ajax({
            dataType    : 'json',
            contentType : 'application/json; charset=utf-8',
            data        : JSON.stringify({id : answer.answerId}),
            success : function(){
                self.answersList.remove(answer);
            },
            error : function(){
                alert("Не удалось удалить ответ");
            }
        });
    };

    ko.computed(function() {
        self.getDiagrams();
        self.getList();
        return self.pageSize() + self.sorter();
    });
};

$( document ).ready(function() {
    ko.applyBindings(new InterviewList());
});

$(function () {
    $('[data-toggle="tooltip"]').tooltip()
});
