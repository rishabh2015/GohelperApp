package in.gohelper.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.gohelper.fragment.ServiceQuestionFragment;
import in.gohelper.models.servicemodels.Question;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private List<Question> serviceQuestions;
    private Map<Question, Fragment> fragmentMap = new HashMap();

    private PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public PagerAdapter(FragmentManager fm, List<Question> serviceQuestions) {
        this(fm);
        this.serviceQuestions = serviceQuestions;
    }

    @Override
    public Fragment getItem(int position) {
        Question question = serviceQuestions.get(position);
        Fragment fragment = fragmentMap.get(question);
        if(fragment == null) {
            fragment = ServiceQuestionFragment.newInstance(question);
            fragmentMap.put(question, fragment);
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return serviceQuestions.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }
}
