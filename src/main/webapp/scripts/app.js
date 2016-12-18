angular.module('jobzz', ['ngRoute', 'ngAnimate', 'ngMaterial', 'ngMessages', 'http-auth-interceptor', 'LocalStorageModule', 'ngMap'])
    .config(function ($routeProvider, $httpProvider, USER_ROLES) {

        $routeProvider
            .when('/', {
                redirectTo: '/login'
            })
            .when('/login', {
                templateUrl: '/views/login.html',
                controller: 'LoginCtrl',
                controllerAs: 'LoginCtrl',
                access: {
                    loginRequired: false,
                    authorizedRoles: [USER_ROLES.all]
                }
            })
            .when('/register/employee', {
                templateUrl: '/views/register/employee.html',
                controller: 'RegisterEmployeeCtrl',
                controllerAs: 'RegisterEmployeeCtrl',
                access: {
                    loginRequired: false,
                    authorizedRoles: [USER_ROLES.all]
                }
            })
            .when('/register/employer', {
                templateUrl: '/views/register/employer.html',
                controller: 'RegisterEmployerCtrl',
                controllerAs: 'RegisterEmployerCtrl',
                access: {
                    loginRequired: false,
                    authorizedRoles: [USER_ROLES.all]
                }
            })
            .when('/employer/home', {
                templateUrl: '/views/employer/home.html',
                controller: 'HomeEmployerCtrl',
                controllerAs: 'HomeEmployerCtrl',
                access: {
                    loginRequired: true,
                    authorizedRoles: [USER_ROLES.employer]
                }
            })
            .when('/employer/post/details', {
                templateUrl: '/views/employer/postDetails.html',
                controller: 'PostDetailsEmployerCtrl',
                controllerAs: 'PostDetailsEmployerCtrl',
                access: {
                    loginRequired: true,
                    authorizedRoles: [USER_ROLES.employer]
                }
            })
            .when('/employer/profile', {
                templateUrl: '/views/employer/profile.html',
                controller: 'ProfileEmployerCtrl',
                controllerAs: 'ProfileEmployerCtrl',
                access: {
                    loginRequired: true,
                    authorizedRoles: [USER_ROLES.employer]
                }
            })
            .when('/employer/settings', {
                templateUrl: '/views/employer/settings.html',
                controller: 'SettingsEmployerCtrl',
                controllerAs: 'SettingsEmployerCtrl',
                access: {
                    loginRequired: true,
                    authorizedRoles: [USER_ROLES.employer]
                }
            })
            .when('/employee/home', {
                templateUrl: '/views/employee/home.html',
                controller: 'HomeEmployeeCtrl',
                controllerAs: 'HomeEmployeeCtrl',
                access: {
                    loginRequired: true,
                    authorizedRoles: [USER_ROLES.employee]
                }
            })
            .when('/employee/profile', {
                templateUrl: '/views/employee/profile.html',
                controller: 'ProfileEmployeeCtrl',
                controllerAs: 'ProfileEmployeeCtrl',
                access: {
                    loginRequired: true,
                    authorizedRoles: [USER_ROLES.employee]
                }
            })
            .when('/employee/settings', {
                templateUrl: '/views/employee/settings.html',
                controller: 'SettingsEmployeeCtrl',
                controllerAs: 'SettingsEmployeeCtrl',
                access: {
                    loginRequired: true,
                    authorizedRoles: [USER_ROLES.employee]
                }
            })
            .when('/employee/find/job', {
                templateUrl: '/views/employee/findJob.html',
                controller: 'FindJobEmployeeCtrl',
                controllerAs: 'FindJobEmployeeCtrl',
                access: {
                    loginRequired: true,
                    authorizedRoles: [USER_ROLES.employee]
                }
            })
            .when('/employee/job/details', {
                templateUrl: '/views/employee/jobDetails.html',
                controller: 'JobDetailsEmployeeCtrl',
                controllerAs: 'JobDetailsEmployeeCtrl',
                access: {
                    loginRequired: true,
                    authorizedRoles: [USER_ROLES.employee]
                }
            })
            .when('/employee/post/details', {
                templateUrl: '/views/employee/postDetails.html',
                controller: 'PostDetailsEmployeeCtrl',
                controllerAs: 'PostDetailsEmployeeCtrl',
                access: {
                    loginRequired: true,
                    authorizedRoles: [USER_ROLES.employee]
                }
            })
            .when('/loading', {
                templateUrl: 'views/loading.html',
                access: {
                    loginRequired: false,
                    authorizedRoles: [USER_ROLES.all]
                }
            })
            .when("/error/:code", {
                templateUrl: "/views/error.html",
                controller: "ErrorCtrl",
                access: {
                    loginRequired: false,
                    authorizedRoles: [USER_ROLES.all]
                }
            })
            .otherwise({
                redirectTo: '/error/404',
                access: {
                    loginRequired: false,
                    authorizedRoles: [USER_ROLES.all]
                }
            });

        $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';

    })
    .run(function ($rootScope, $location, $http, EmployerSession, EmployeeSession, EmployerAuthSharedService, EmployeeAuthSharedService, $q, $timeout, localStorageService) {


        $rootScope.$on('event:auth-forbidden', function (rejection) {
            $rootScope.$evalAsync(function () {
                $location.path('/error/403').replace();
            });
        });

        // Call when the the client is confirmed
        $rootScope.$on('event:auth-loginConfirmed', function (event, data) {
            $rootScope.isEmployee = localStorageService.get("isEmployee");
            $rootScope.isEmployer = localStorageService.get("isEmployer");
            $rootScope.loadingAccount = false;
            var home = $rootScope.isEmployee ? "/employee/home" : "/employer/home";
            var nextLocation = ($rootScope.requestedUrl ? $rootScope.requestedUrl : home);
            var delay = ($location.path() === "/loading" ? 1500 : 0);

            $timeout(function () {

                if ($rootScope.isEmployee) {
                    EmployeeSession.create(data);
                    $rootScope.account = EmployeeAuthSharedService;
                } else if ($rootScope.isEmployer) {
                    EmployerSession.create(data);
                    $rootScope.account = EmployerAuthSharedService;
                }

                $rootScope.authenticated = true;
                $location.path(nextLocation).replace();
            }, delay);

        });

        // Call when the 401 response is returned by the server
        $rootScope.$on('event:auth-loginRequired', function (event, data) {
            if ($rootScope.loadingAccount && data.status !== 401) {
                $rootScope.requestedUrl = $location.path();
                $location.path('/loading');
            } else {

                EmployeeSession.invalidate();
                EmployerSession.invalidate();

                $rootScope.authenticated = false;
                $rootScope.loadingAccount = false;
                $location.path('/login');
            }
        });

        $rootScope.$on('$routeChangeStart', function (event, next) {
            if (next.originalPath === "/login" && $rootScope.authenticated) {
                event.preventDefault();
            } else if (next.access && next.access.loginRequired && !$rootScope.authenticated) {
                event.preventDefault();
                $rootScope.$broadcast("event:auth-loginRequired", {});
            } else if (next.access && (!EmployerAuthSharedService.isAuthorized(next.access.authorizedRoles) && !EmployeeAuthSharedService.isAuthorized(next.access.authorizedRoles))) {
                event.preventDefault();
                $rootScope.$broadcast("event:auth-forbidden", {});
            }
        });

        // Call when the user logs out
        $rootScope.$on('event:auth-loginCancelled', function () {
            $location.path('/login').replace();
        });

        $rootScope.isEmployee = localStorageService.get("isEmployee");
        $rootScope.isEmployer = localStorageService.get("isEmployer");

        // Get already authenticated user account
        if ($rootScope.isEmployee) {
            EmployeeAuthSharedService.getAccount();
        } else if ($rootScope.isEmployer) {
            EmployerAuthSharedService.getAccount();
        }
    });