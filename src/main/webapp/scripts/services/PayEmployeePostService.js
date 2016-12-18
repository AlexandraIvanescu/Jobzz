angular.module('jobzz').service('payEmployeePostService', function () {
    var post = {};
    var postIsPaid = false;

    var setPost = function (newObj) {
        post = newObj;
    };

    var getPost = function () {
        return post;
    };

    var setPostIsPaid = function (value) {
        postIsPaid = value;
    };

    var getPostIsPaid = function () {
        return postIsPaid;
    };

    return {
        setPost: setPost,
        getPost: getPost,
        setPostIsPaid: setPostIsPaid,
        getPostIsPaid: getPostIsPaid
    };

});
