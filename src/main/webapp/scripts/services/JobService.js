angular.module('jobzz').service('jobService', function () {
    var job = {};

    var setJob = function (newObj) {
        job = newObj;
    };

    var getJob = function () {
        return job;
    };

    return {
        setJob: setJob,
        getJob: getJob
    };

});
