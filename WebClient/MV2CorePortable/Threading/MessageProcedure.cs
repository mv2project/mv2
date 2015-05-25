using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;


namespace ISS.MV2.Threading {

    public delegate void ProcedureUpdateDelegate<P, R>(MessageProcedure<P, R> sender, string state, int progress);
    public delegate void ProcedureFailedDelegate<P, R>(MessageProcedure<P, R> sender, Exception ex);
    public delegate void ProcedureCompletedDelegate<P, R>(MessageProcedure<P, R> sender, R result);


    public abstract class MessageProcedure<P, R> {

        public event ProcedureCompletedDelegate<P, R> Completed;
        public event ProcedureFailedDelegate<P, R> Failed;
        public event ProcedureUpdateDelegate<P, R> Updated;

        private bool running = false;
        private object lock_object = new object();

        private IEventDispatcher eventDispatcher;

        public IEventDispatcher EventDispatcher {
            get {
                return eventDispatcher;
            }
            set {
                eventDispatcher = value;
            }
        }

        private P parameter;

        private string state = "";
        private int progress = 0;

        public string State { get { return state; } }
        public int Progress { get { return progress; } }

        protected P Parameter {
            get {
                return parameter;
            }
            set {
                parameter = value;
            }
        }

        public MessageProcedure(IEventDispatcher dispatcher, P parameter) {
            this.eventDispatcher = dispatcher;
        }


        protected abstract R DoProcedure(P parameter);

        private void NotifyUpdate() {
            if (Updated == null) return;
            eventDispatcher.Invoke(new EventInvokationDelegate(() => {
                Updated(this, state, progress);
            }));
        }

        protected void Update(int progress) {
            this.progress = progress;
            NotifyUpdate();
        }

        protected void Update(string state) {
            this.state = state;
            NotifyUpdate();
        }

        protected void Update(string state, int progress) {
            this.state = state;
            this.progress = progress;
            NotifyUpdate();
        }

        public void Execute() {
            lock (lock_object) {
                if (running) return;
                running = true;
                Task t = new Task(new Action<object>((param) => {
                    try {
                        R result = DoProcedure((P)param);
                        NotifySuccess(result);
                    } catch(Exception ex) {
                        NotifyFail(ex);
                    }
                    lock (lock_object) {
                        running = false;
                    }
                }), parameter);
                t.Start();
            }
        }

        private void NotifySuccess(R result) {
            if (Completed == null) return;
            eventDispatcher.Invoke(new EventInvokationDelegate(() => {
                Completed(this, result);
            }));
        }

        private void NotifyFail(Exception ex) {
            if (Failed == null) return;
            eventDispatcher.Invoke(new EventInvokationDelegate(() => {
                Failed(this, ex);
            }));
        }

        public R ExecuteImmediate() {
            return DoProcedure(parameter);
        }

    }

}
